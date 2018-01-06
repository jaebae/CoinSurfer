package com.sungjae.coinsurfer.service;


import android.app.Service;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.sungjae.coinsurfer.exchange.Exchange;
import com.sungjae.coinsurfer.exchange.ExchangeFactory;
import com.sungjae.coinsurfer.notification.TradeNotification;
import com.sungjae.coinsurfer.setting.TradeSetting;
import com.sungjae.coinsurfer.tradedata.Balance;
import com.sungjae.coinsurfer.tradedata.Coin;
import com.sungjae.coinsurfer.tradedata.CoinType;
import com.sungjae.coinsurfer.tradedata.TooBigDiffRateException;
import com.sungjae.coinsurfer.tradedata.TradeInfo;
import com.sungjae.coinsurfer.tradedata.TradeModel;

import java.util.ArrayList;

import static com.sungjae.coinsurfer.tradedata.TradeInfo.TradeType.BUY;
import static com.sungjae.coinsurfer.tradedata.TradeInfo.TradeType.SELL;

public class TradeService extends Service implements TradeSetting.OnSettingChangeListener {

    private final long BALANCE_LOG_INTERVAL = 1000L * 60L * 5L;
    private Handler mHandler;
    private TradeSetting mTradeSetting;
    private Exchange mExchange;
    private Balance mBalance;
    private TradeModel mTradeModel;
    private Object SETTING_LOCK = new Object();
    private long mLastBalanceLogTime = 0L;

    private TradeNotification mTradeNotification;

    @Override
    public void onCreate() {
        super.onCreate();
        mTradeSetting = TradeSetting.getInstance(getApplicationContext());
        mTradeSetting.addOnChangedListener(this); // it don't need to unregister

        mExchange = ExchangeFactory.createBithumbExchange();
        mBalance = Balance.getsInstance();
        mTradeModel = TradeModel.getInstance();

        mTradeNotification = new TradeNotification(getApplicationContext());

        applySettingValue();

        createHandler();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mHandler.sendEmptyMessage(0);
        return super.onStartCommand(intent, flags, startId);
    }

    @NonNull
    private Handler createHandler() {
        mHandler = new Handler(getLooperHandlerThread()) {
            @Override
            public void handleMessage(Message msg) {
                long interval = mTradeSetting.getPollingTime() * 1000;
                removeMessages(0);
                doTradeLogic();
                sendEmptyMessageDelayed(0, interval);
            }
        };
        return mHandler;
    }

    private void doTradeLogic() {
        try {
            synchronized (SETTING_LOCK) {
                mExchange.getMarketPrice(mBalance);
                mExchange.getBalance(mBalance);

                ArrayList<TradeInfo> tradeList = mTradeModel.getTradeInfoList();
                if (!tradeList.isEmpty()) {
                    trade(tradeList, SELL); //sell should do first to get KRW for Buy
                    trade(tradeList, BUY);
                    mLastBalanceLogTime = 0L;
                }

                showNotifyMsg(getBalanceInfo());
            }
        } catch (TooBigDiffRateException e) {
            showNotifyMsg(e.getMessage());
            ArrayList<String> msgList = mExchange.getLastReceivedData();
            for (String msg : msgList) {
                writeErrLog(msg);
            }
        } catch (Exception e) {
            showNotifyMsg(e.getMessage());
        } finally {
            writeBalanceLog(mBalance);
            updateView();
        }
    }

    private void writeErrLog(String msg) {
        ContentResolver cr = getContentResolver();

        Uri uri = Uri.parse("content://coinsurfer/err_log");
        ContentValues contentValue = new ContentValues();

        contentValue.put("log", msg);
        Uri result = cr.insert(uri, contentValue);
    }

    private String getBalanceInfo() {
        return String.format("%,.0f", mBalance.getTotalAsKrw());
    }

    private void showNotifyMsg(String msg) {
        startForeground(1, mTradeNotification.showNotification(msg));
    }

    private void trade(ArrayList<TradeInfo> tradeList, TradeInfo.TradeType tradeType) throws Exception {
        for (TradeInfo tradeInfo : tradeList) {
            if (tradeInfo.getTradeType() == tradeType) {
                TradeInfo tradeResult = mExchange.trade(tradeInfo);
                writeTradeLog(tradeResult);
            }
        }
    }

    private void writeBalanceLog(Balance balance) {
        if (mBalance.getKrw() <= 0) {
            return;
        }

        long curTime = System.currentTimeMillis();

        if (curTime - mLastBalanceLogTime > BALANCE_LOG_INTERVAL) {
            mLastBalanceLogTime = curTime;

            ContentResolver cr = getContentResolver();

            writeKrwLog(cr, curTime, balance);
            writeCoinLog(cr, curTime, balance);

            Uri uri = Uri.parse("content://coinsurfer/balance_total");
            cr.notifyChange(uri, null);
        }
    }

    private void writeCoinLog(ContentResolver cr, long curTime, Balance balance) {
        int cnt = balance.getCoinCount();
        for (int i = 0; i < cnt; i++) {
            Coin coin = balance.getCoin(i);
            Uri uri = Uri.parse("content://coinsurfer/balance");
            ContentValues contentValue = new ContentValues();

            contentValue.put("date", curTime);
            contentValue.put("coin", coin.getCoinType().ordinal());
            contentValue.put("price", coin.getCurPrice());
            contentValue.put("amount", coin.getCoinValue());
            contentValue.put("krw", coin.getCurKrw());

            Uri result = cr.insert(uri, contentValue);
        }
    }

    private void writeKrwLog(ContentResolver cr, long curTime, Balance balance) {
        Uri uri = Uri.parse("content://coinsurfer/balance");
        ContentValues contentValue = new ContentValues();

        contentValue.put("date", curTime);
        contentValue.put("coin", "-1");
        contentValue.put("price", 1);
        contentValue.put("amount", balance.getKrw());
        contentValue.put("krw", balance.getKrw());

        Uri result = cr.insert(uri, contentValue);
    }

    private void writeTradeLog(TradeInfo tradeResult) {
        Uri uri = Uri.parse("content://coinsurfer/trade");
        ContentValues contentValue = new ContentValues();
        contentValue.put("date", System.currentTimeMillis());
        contentValue.put("trade", tradeResult.getTradeType().ordinal());
        contentValue.put("coin", tradeResult.getCoinType().ordinal());
        contentValue.put("unit", tradeResult.getTradeCoinAmount());
        contentValue.put("price", tradeResult.getTradePrice());
        contentValue.put("krw", tradeResult.getTradeKrw());

        ContentResolver cr = getContentResolver();
        Uri result = cr.insert(uri, contentValue);
    }

    @NonNull
    private Looper getLooperHandlerThread() {
        HandlerThread handlerThread = new HandlerThread("BG_THREAD");
        handlerThread.start();
        return handlerThread.getLooper();
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onSettingChange() {
        applySettingValue();
    }

    private void applySettingValue() {
        synchronized (SETTING_LOCK) {
            mBalance.clearCoinInfo();
            ArrayList<CoinType> coinTypeList = mTradeSetting.getEnableCoinList();
            for (CoinType coinType : coinTypeList) {
                mBalance.updateCoin(new Coin(coinType));
            }

            mTradeModel.setCoinRate(mTradeSetting.getCoinRate());
            mTradeModel.setTriggerRate(mTradeSetting.getTriggerRate());

            mExchange.setApiKey(mTradeSetting.getConnectKey(), mTradeSetting.getSecretKey());
        }
    }

    private void updateView() {
        Intent intent = new Intent("UPDATE_VIEW");
        getApplicationContext().sendBroadcast(intent);
    }
}
