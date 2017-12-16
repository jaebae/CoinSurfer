package com.sungjae.coinsurfer.exchange;

public class Balance {
    private float mBitMoney;
    private float mRealMoney;

    public Balance(float bitMoney, float realMoney) {
        mBitMoney = bitMoney;
        mRealMoney = realMoney;
    }

    public float getBitAmount() {
        return mBitMoney;
    }

    public float getRealMoney() {
        return mRealMoney;
    }

    @Override
    public String toString() {
        return "mBitMoney = " + getBitAmount() + "\nmRealMoeny = " + getRealMoney();
    }
}
