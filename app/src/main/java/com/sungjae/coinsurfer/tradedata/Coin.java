package com.sungjae.coinsurfer.tradedata;


public class Coin {
    private CoinType mCoinType;
    private double mCoinValue;
    private double mBuyPrice;  /*구매가*/
    private double mSellPrice; /*판매가*/
    private double mCurPrice; /*마지막 거래가*/

    public Coin(int coinType) {
        mCoinType = CoinType.getCoinType(coinType);
    }

    public Coin(CoinType coinType) {
        mCoinType = coinType;
    }

    public void setCoinValue(double coinValue) {
        mCoinValue = coinValue;
    }

    public void setBuyPrice(double buyPrice) {
        mBuyPrice = buyPrice;
    }

    public void setSellPrice(double sellPrice) {
        mSellPrice = sellPrice;
    }

    public void setCurPrice(double curPrice) {
        mCurPrice = curPrice;
    }

    public double getBuyKrw() {
        return mCoinValue * mBuyPrice;
    }

    public double getSellKrw() {
        return mCoinValue * mSellPrice;
    }

    public double getCurKrw() {
        return mCoinValue * mCurPrice;
    }

    public String getCoinName() {
        return mCoinType.toString();
    }

    public double getBuyCoin(double krw) {
        return krw / mBuyPrice;
    }

    public double getSellCoin(double krw) {
        return krw / mSellPrice;
    }

    public CoinType getCoinType() {
        return mCoinType;
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
}
