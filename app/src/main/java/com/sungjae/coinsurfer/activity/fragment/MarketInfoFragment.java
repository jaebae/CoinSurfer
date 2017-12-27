package com.sungjae.coinsurfer.activity.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.sungjae.coinsurfer.setting.TradeSetting;
import com.sungjae.coinsurfer.tradedata.CoinType;

import java.util.ArrayList;


public class MarketInfoFragment extends Fragment implements TradeSetting.OnSettingChangeListener {
    TradeSetting mTradeSetting;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTradeSetting = TradeSetting.getInstance(getContext());
        mTradeSetting.addOnChangedListener(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mTradeSetting.removeOnChangedListener(this);
    }

    @Override
    public void onSettingChange() {
        float investKrw = mTradeSetting.getInvestKrw();
        ArrayList<CoinType> enableCoin = mTradeSetting.getEnableCoinList();

        int i = 0;
        i++;
    }
}
