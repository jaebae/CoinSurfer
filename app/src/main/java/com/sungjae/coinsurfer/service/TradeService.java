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

import com.sungjae.coinsurfer.setting.TradeSetting;

public class TradeService extends Service implements TradeSetting.OnSettingChangeListener {

    private Handler mHandler;
    private TradeSetting mTradeSetting;

    @Override
    public void onCreate() {
        super.onCreate();
        mTradeSetting = TradeSetting.getInstance(getApplicationContext());
        mTradeSetting.addOnChangedListener(this); // it don't need to unregister
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

    }
}
