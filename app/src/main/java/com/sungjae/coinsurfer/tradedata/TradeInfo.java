package com.sungjae.coinsurfer.tradedata;


import static com.sungjae.coinsurfer.tradedata.TradeInfo.TradeType.HOLD;

public class TradeInfo {
    private TradeType mTradeType;
    private CoinType mCoinType;
    private double mTradeKrw;
    private double mTradeCoinAmount;

    public TradeInfo(CoinType coinType) {
        mTradeType = HOLD;
        mCoinType = coinType;
    }

    public String getCoinName() {
        return mCoinType.toString();
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

    public String getTradeCoinAmount() {
        return makeAsTradeUnit(mTradeCoinAmount);
    }

    public void setTradeCoinAmount(double amount) {
        mTradeCoinAmount = amount;
    }

    public double getTradePrice() {
        return mTradeKrw / mTradeCoinAmount;
    }

    public double getTradeKrw() {
        return mTradeKrw;
    }

    public void setTradeKrw(double krw) {
        mTradeKrw = krw;
    }

    public String makeAsTradeUnit(double value) {
        if (mCoinType.getTradeUnit() == 0.1) {
            return String.format("%.1f", value);
        } else if (mCoinType.getTradeUnit() == 0.01) {
            return String.format("%.2f", value);
        } else if (mCoinType.getTradeUnit() == 0.001) {
            return String.format("%.3f", value);
        } else if (mCoinType.getTradeUnit() == 1) {
            return String.format("%.0f", value);
        } else if (mCoinType.getTradeUnit() == 10) {
            String ret = String.format("%.0f", (value / 10));
            return ret + "0";
        }

        return "";
    }

    public enum TradeType {
        HOLD, BUY, SELL
    }
}
