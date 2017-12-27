package com.sungjae.coinsurfer.tradedata;


import static com.sungjae.coinsurfer.tradedata.TradeInfo.TradeType.HOLD;

public class TradeInfo {
    private CoinType mCoinType;
    private TradeType mTradeType;
    private double mTradeCoinAmount;
    private double mTradeKrw;

    TradeInfo(CoinType coinType) {
        mTradeType = HOLD;
        mCoinType = coinType;
    }

    public CoinType getCoinType() {
        return mCoinType;
    }

    public TradeType getTradeType() {
        return mTradeType;
    }

    public void setTradeType(TradeType tradeType) {
        mTradeType = tradeType;
    }

    public double getTradeCoinAmount() {
        return mTradeCoinAmount;
    }

    public void setTradeCoinAmount(double amount) {
        mTradeCoinAmount = amount;
    }

    public double getTradeKrw() {
        return mTradeKrw;
    }

    public void setTradeKrw(double krw) {
        mTradeKrw = krw;
    }

    public enum TradeType {
        HOLD, BUY, SELL
    }

}
