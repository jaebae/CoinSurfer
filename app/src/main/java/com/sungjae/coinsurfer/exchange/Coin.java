package com.sungjae.coinsurfer.exchange;

import java.util.HashMap;

public class Coin {

    private final static Coin mInstance = new Coin();

    private HashMap<String, Float> mCoinMap = new HashMap<>();

    private Coin() {
        mCoinMap.put("BTC", 0.001f);
        mCoinMap.put("ETH", 0.01f);
        mCoinMap.put("DASH", 0.01f);
        mCoinMap.put("LTC", 0.1f);
        mCoinMap.put("ETC", 0.1f);
        mCoinMap.put("XRP", 10.f);
        mCoinMap.put("BCH", 0.001f);
        mCoinMap.put("XMR", 0.01f);
        mCoinMap.put("ZEC", 0.001f);
        mCoinMap.put("QTUM", 0.1f);
        mCoinMap.put("BTG", 0.01f);
        mCoinMap.put("EOS", 1.0f);
    }

    public static Coin getInstance() {
        return mInstance;
    }
}
