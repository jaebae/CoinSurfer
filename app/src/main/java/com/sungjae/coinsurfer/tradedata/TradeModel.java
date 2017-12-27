package com.sungjae.coinsurfer.tradedata;


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
        ArrayList<TradeInfo> tradeInfoList = new ArrayList<>();

        float totalAsKrw = mBalance.getTotalAsKrw();

        float targetCoin = (totalAsKrw * mCoinRate) / mBalance.getCoinCount();

        for (int i = 0; i < mBalance.getCoinCount(); i++) {
            Coin coin = mBalance.getCoin(i);

            TradeInfo tradeInfo;
            if (targetCoin > coin.getCurKrw()) {
                tradeInfo = createBuyTradeInfo(coin, targetCoin);
            } else {
                tradeInfo = createSellTradeInfo(coin, targetCoin);
            }

            if (tradeInfo.getTradeType() != TradeInfo.TradeType.HOLD) {
                tradeInfoList.add(tradeInfo);
            }
        }

        return tradeInfoList;
    }

    private TradeInfo createBuyTradeInfo(Coin coin, float targetCoin) {
        float diff = targetCoin - coin.getBuyKrw();
        float rate = (diff / targetCoin) * 100.f;

        TradeInfo tradeInfo = new TradeInfo(coin.getCoinType());

        if (rate > mTriggerRate) {
            tradeInfo.setTradeType(TradeInfo.TradeType.BUY);
            tradeInfo.setTradeAmount(coin.getBuyCoin(diff));
        }
        return tradeInfo;
    }

    private TradeInfo createSellTradeInfo(Coin coin, float targetCoin) {
        float diff = coin.getSellKrw() - targetCoin;
        float rate = (diff / targetCoin) * 100.f;
        TradeInfo tradeInfo = new TradeInfo(coin.getCoinType());
        if (rate > mTriggerRate) {
            tradeInfo.setTradeType(TradeInfo.TradeType.SELL);
            tradeInfo.setTradeAmount(coin.getSellCoin(diff));
        }
        return tradeInfo;
    }

}
