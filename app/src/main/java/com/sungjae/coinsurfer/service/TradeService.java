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

import java.util.ArrayList;

public class TradeService extends Service implements TradeSetting.OnSettingChangeListener {

    private Handler mHandler;
    private TradeSetting mTradeSetting;

    private Exchange mExchange;
    private Balance mBalance;

    @Override
    public void onCreate() {
        super.onCreate();
        mTradeSetting = TradeSetting.getInstance(getApplicationContext());
        mTradeSetting.addOnChangedListener(this); // it don't need to unregister

        mExchange = ExchangeFactory.createBithumbExchange();
        mExchange.setApiKey(mTradeSetting.getConnectKey(), mTradeSetting.getSecretKey());

        mBalance = new Balance();
        initBalanceCoin();

        getHandler();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mHandler.sendEmptyMessage(0);
        return super.onStartCommand(intent, flags, startId);
    }

    @NonNull
    private Handler getHandler() {
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
            mExchange.getMarketPrice(mBalance);
            mExchange.getBalance(mBalance);
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
        mExchange.setApiKey(mTradeSetting.getConnectKey(), mTradeSetting.getSecretKey());
        initBalanceCoin();
    }

    private void initBalanceCoin() {
        mBalance.clearCoinInfo();
        ArrayList<CoinType> coinTypeList = mTradeSetting.getEnableCoinList();
        for (CoinType coinType : coinTypeList) {
            mBalance.updateCoin(new Coin(coinType));
        }
    }
}
