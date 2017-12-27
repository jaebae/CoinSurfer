package com.sungjae.coinsurfer.tradedata;


import static com.sungjae.coinsurfer.tradedata.TradeInfo.TradeType.HOLD;

public class TradeInfo {
    CoinType mCoinType;
    TradeType mTradeType;
    float mTradeAmount;

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

    public float getTradeAmount() {
        return mTradeAmount;
    }

    public void setTradeAmount(float amount) {
        mTradeAmount = amount;
    }

    public enum TradeType {
        HOLD, BUY, SELL
    }

}
