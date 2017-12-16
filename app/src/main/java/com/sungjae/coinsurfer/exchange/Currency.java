package com.sungjae.coinsurfer.exchange;

public class Currency {
    private String mCoinCode;
    private float mBuy;
    private float mSell;


    public Currency(String coinType, float buy, float sell) {
        mCoinCode = coinType;
        mBuy = buy;
        mSell = sell;
    }

    @Override
    public String toString() {
        return mCoinCode + " : Buy = " + mBuy + " Sell = " + mSell;
    }

    public String getCoinCode() {
        return mCoinCode;
    }

    public float getBuy() {
        return mBuy;
    }

    public float getSell() {
        return mSell;
    }

}