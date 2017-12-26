package com.sungjae.coinsurfer.exchange;


public class Coin {
    private CoinType mCoinType;
    private float mCoinValue;
    private float mBuyPrice;
    private float mSellPrice;

    public Coin(int coinType) {
        mCoinType = CoinType.getCoinType(coinType);
    }

    public void setCoinValue(float coinValue) {
        mCoinValue = coinValue;
    }

    public void setBuyPrice(float buyPrice) {
        mBuyPrice = buyPrice;
    }

    public void setSellPrice(float sellPrice) {
        mSellPrice = sellPrice;
    }

    public float getBuyKrw() {
        return mCoinValue * mBuyPrice;
    }

    public float getSellKrw() {
        return mCoinValue * mSellPrice;
    }

    public String getCoinName() {
        return mCoinType.toString();
    }
}
