package com.sungjae.coinsurfer.activity.fragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

import com.sungjae.coinsurfer.setting.TradeSetting;

abstract public class BaseFragment extends Fragment implements TradeSetting.OnSettingChangeListener {
    protected TradeSetting mTradeSetting;
    BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateView();
        }
    };

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    abstract protected void updateView();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mTradeSetting = TradeSetting.getInstance(getContext());
    }

    @Override
    public void onResume() {
        super.onResume();
        mTradeSetting.addOnChangedListener(this);
        registerBroadcastReceivers();
        updateView();
    }

    @Override
    public void onPause() {
        super.onPause();
        mTradeSetting.removeOnChangedListener(this);
        unregisterBroadcastReceivers();
    }

    private void registerBroadcastReceivers() {
        IntentFilter filter = new IntentFilter("UPDATE_VIEW");
        getContext().registerReceiver(mBroadcastReceiver, filter);
    }

    private void unregisterBroadcastReceivers() {
        getContext().unregisterReceiver(mBroadcastReceiver);
    }

    @Override
    public void onSettingChange() {
        updateView();
    }
}
