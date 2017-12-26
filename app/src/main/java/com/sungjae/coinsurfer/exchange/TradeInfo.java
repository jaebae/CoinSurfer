package com.sungjae.coinsurfer.exchange;


public class TradeInfo {
    CoinType mCoinType;
    TradeType mTradeType;
    float mTradeAmount;

    public CoinType getCoinType() {
        return mCoinType;
    }

    public TradeType getTradeType() {
        return mTradeType;
    }

    public float getTradeAmount() {
        return mTradeAmount;
    }

    public enum TradeType {
        HOLD, BUY, SELL
    }

}
