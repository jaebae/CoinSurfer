package com.sungjae.coinsurfer.exchange;


import java.util.ArrayList;

import static com.sungjae.coinsurfer.exchange.TradeInfo.TradeType.BUY;
import static com.sungjae.coinsurfer.exchange.TradeInfo.TradeType.HOLD;
import static com.sungjae.coinsurfer.exchange.TradeInfo.TradeType.SELL;

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
        ArrayList<TradeInfo> tradeInfoList = new ArrayList<>();

        float totalAsKrw = mBalance.getTotalAsKrw();

        float targetCoin = (totalAsKrw * mCoinRate) / mBalance.getCoinCount();

        for (int i = 0; i < mBalance.getCoinCount(); i++) {
            Coin coin = mBalance.getCoin(i);
            TradeInfo tradeInfo = new TradeInfo(coin.getCoinType());
            if (targetCoin > coin.getCurKrw()) {
                float diff = targetCoin - coin.getBuyKrw();
                float rate = (diff / targetCoin) * 100.f;
                if (rate > mTriggerRate) {
                    tradeInfo.setTradeType(BUY);
                    tradeInfo.setTradeAmount(coin.getBuyCoin(diff));
                }
            } else {
                float diff = coin.getSellKrw() - targetCoin;
                float rate = (diff / targetCoin) * 100.f;
                if (rate > mTriggerRate) {
                    tradeInfo.setTradeType(SELL);
                    tradeInfo.setTradeAmount(coin.getSellCoin(diff));
                }
            }

            if (tradeInfo.getTradeType() != HOLD) {
                tradeInfoList.add(tradeInfo);
            }
        }

        return tradeInfoList;
    }
}
