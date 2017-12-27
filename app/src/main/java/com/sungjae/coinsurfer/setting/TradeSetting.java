package com.sungjae.coinsurfer.setting;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.preference.PreferenceManager;

import com.sungjae.coinsurfer.tradedata.CoinType;

import java.util.ArrayList;

/**
 * Created by 배성재 on 2017-12-27.
 */

public class TradeSetting implements SharedPreferences.OnSharedPreferenceChangeListener {
    private static TradeSetting sTradeSettingInstance;
    private Context mContext;
    private SharedPreferences mSharedPreferences;
    private ArrayList<OnSettingChangeListener> mOnSettingChangeListener = new ArrayList<>();
    private float mCoinRate;
    private float mKrwRate;
    private float mInvestKrw;
    private String mConnectKey;
    private String mSecretKey;
    private ArrayList<CoinType> mEnableCoinList = new ArrayList<>();
    private long mPollingTime;

    private TradeSetting(Context context) {
        mContext = context;
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        mSharedPreferences.registerOnSharedPreferenceChangeListener(this); //it don't need to unregister
        updateValue();
    }

    public synchronized static TradeSetting getInstance(Context context) {
        if (sTradeSettingInstance == null) {
            sTradeSettingInstance = new TradeSetting(context);
        }

        return sTradeSettingInstance;
    }

    public float getInvestKrw() {
        return mInvestKrw;
    }

    public ArrayList<CoinType> getEnableCoinList() {
        return mEnableCoinList;
    }

    public long getPollingTime() {
        return mPollingTime;
    }

    public void addOnChangedListener(OnSettingChangeListener onSettingChangeListener) {
        mOnSettingChangeListener.add(onSettingChangeListener);
    }

    public void removeOnChangedListener(OnSettingChangeListener onSettingChangeListener) {
        mOnSettingChangeListener.remove(onSettingChangeListener);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        updateValue();

        for (OnSettingChangeListener listener : mOnSettingChangeListener) {
            listener.onSettingChange();
        }
    }

    private void updateValue() {
        updateConnection();
        updateInvestInfo();
        updateEnableCoinList();
    }

    private void updateEnableCoinList() {
        int coinCount = CoinType.values().length;

        mEnableCoinList.clear();

        for (int i = 0; i < coinCount; i++) {
            String coinKey = "coin_" + i;

            if (mSharedPreferences.getBoolean(coinKey, false)) {
                mEnableCoinList.add(CoinType.getCoinType(i));
            }
        }
    }

    private void updateConnection() {
        mConnectKey = mSharedPreferences.getString("connect_key", "");
        mSecretKey = mSharedPreferences.getString("secret_key", "");
    }

    private void updateInvestInfo() {
        mCoinRate = getFloatValue("coin_rate", 0.8f);
        mKrwRate = getFloatValue("krw_rate", 0.2f);

        mInvestKrw = getFloatValue("invest_krw", 1000000f);

        mPollingTime = getLongValue("polling_time", 10L);
    }

    private float getFloatValue(String key, float defaultValue) {
        String value = mSharedPreferences.getString(key, "" + defaultValue);
        return Float.parseFloat(value);
    }

    private long getLongValue(String key, long defaultValue) {
        String value = mSharedPreferences.getString(key, "" + defaultValue);
        return Long.parseLong(value);
    }

    public interface OnSettingChangeListener {
        void onSettingChange();
    }
}
