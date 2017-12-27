package com.sungjae.coinsurfer.service;


import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.sungjae.coinsurfer.exchange.Exchange;
import com.sungjae.coinsurfer.exchange.ExchangeFactory;
import com.sungjae.coinsurfer.setting.TradeSetting;
import com.sungjae.coinsurfer.tradedata.Balance;
import com.sungjae.coinsurfer.tradedata.Coin;
import com.sungjae.coinsurfer.tradedata.CoinType;
import com.sungjae.coinsurfer.tradedata.TradeInfo;
import com.sungjae.coinsurfer.tradedata.TradeModel;

import java.util.ArrayList;

public class TradeService extends Service implements TradeSetting.OnSettingChangeListener {

    private Handler mHandler;
    private TradeSetting mTradeSetting;

    private Exchange mExchange;
    private Balance mBalance;
    private TradeModel mTradeModel;

    private Object SETTING_LOCK = new Object();

    @Override
    public void onCreate() {
        super.onCreate();
        mTradeSetting = TradeSetting.getInstance(getApplicationContext());
        mTradeSetting.addOnChangedListener(this); // it don't need to unregister

        mExchange = ExchangeFactory.createBithumbExchange();
        mBalance = new Balance();
        mTradeModel = new TradeModel();
        mTradeModel.setBalance(mBalance);

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
                for (TradeInfo tradeInfo : tradeList) {
                    mExchange.trade(tradeInfo);
                }
            }
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

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
}
