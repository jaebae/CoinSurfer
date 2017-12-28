package com.sungjae.coinsurfer.activity.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.sungjae.coinsurfer.setting.TradeSetting;
import com.sungjae.coinsurfer.tradedata.Balance;
import com.sungjae.coinsurfer.tradedata.Coin;
import com.sungjae.coinsurfer.tradedata.CoinType;

import java.util.ArrayList;


public class BalanceInfoFragment extends Fragment implements TradeSetting.OnSettingChangeListener {
    private TradeSetting mTradeSetting;
    private Balance mBalance;
    BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateView();
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBalance = Balance.getsInstance();
        mTradeSetting = TradeSetting.getInstance(getContext());
        mTradeSetting.addOnChangedListener(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mTradeSetting.removeOnChangedListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        registerBroadcastReceivers();
        updateView();
    }

    private void updateView() {
        for (int i = 0; i < mBalance.getCoinCount(); i++) {
            Coin coin = mBalance.getCoin(i);

        }

    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterBroadcastReceivers();
    }

    @Override
    public void onSettingChange() {
        double investKrw = mTradeSetting.getInvestKrw();
        ArrayList<CoinType> enableCoin = mTradeSetting.getEnableCoinList();


    }

    private void registerBroadcastReceivers() {
        IntentFilter filter = new IntentFilter("UPDATE_VIEW");
        getContext().registerReceiver(mBroadcastReceiver, filter);

    }

    private void unregisterBroadcastReceivers() {
        getContext().unregisterReceiver(mBroadcastReceiver);
    }
}
