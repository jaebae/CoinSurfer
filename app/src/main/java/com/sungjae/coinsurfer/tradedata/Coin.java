package com.sungjae.coinsurfer.tradedata;


public class Coin {
    private CoinType mCoinType;
    private float mCoinValue;
    private float mBuyPrice;  /*구매가*/
    private float mSellPrice; /*판매가*/
    private float mCurPrice; /*마지막 거래가*/

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

    public void setCurPrice(float curPrice) {
        mCurPrice = curPrice;
    }

    public float getBuyKrw() {
        return mCoinValue * mBuyPrice;
    }

    public float getSellKrw() {
        return mCoinValue * mSellPrice;
    }

    public float getCurKrw() {
        return mCoinValue * mCurPrice;
    }

    public String getCoinName() {
        return mCoinType.toString();
    }

    public float getBuyCoin(float krw) {
        return krw / mBuyPrice;
    }

    public float getSellCoin(float krw) {
        return krw / mSellPrice;
    }

    public CoinType getCoinType() {
        return mCoinType;
    }
}
