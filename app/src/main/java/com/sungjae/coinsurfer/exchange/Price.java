package com.sungjae.coinsurfer.exchange;


public class Price {
    private float mCoin;
    private float mPrice;

    public void setPrice(float price) {
        mPrice = price;
    }

    public void setCoin(float coin) {
        mCoin = coin;
    }

    public float getKrw() {
        return mCoin * mPrice;
    }
}
