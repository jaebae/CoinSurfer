package com.sungjae.coinsurfer.exchange;


import java.util.ArrayList;

public class TradeModel {
    float mKrwRate;
    float mCoinRate;

    float mTriggerRate;

    Balance mBalance;

    public void setBalance(Balance balance) {
        mBalance = balance;
    }

    public void setCoinRate(float coinRate) {
        mCoinRate = coinRate;
    }

    public void setKrwRate(float krwRate) {
        mKrwRate = krwRate;
    }

    public void setTriggerRate(float triggerRate) {
        mTriggerRate = triggerRate;
    }

    public ArrayList<TradeInfo> getTradeInfoList() {
        ArrayList<TradeInfo> tradeInfo = new ArrayList<>();

        float totalAsKrw = mBalance.getTotalAsKrw();

        float targetCoin = (totalAsKrw * mCoinRate) / mBalance.getCoinCount();

        for (int i = 0; i < mBalance.getCoinCount(); i++) {
            Coin coin = mBalance.getCoin(i);
            coin.getBuyKrw();
        }

        return null;
    }
}
