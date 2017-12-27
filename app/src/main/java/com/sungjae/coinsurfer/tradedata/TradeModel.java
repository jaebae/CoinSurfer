package com.sungjae.coinsurfer.tradedata;


import java.util.ArrayList;

public class TradeModel {

    double mCoinRate;

    double mTriggerRate;

    Balance mBalance;

    public void setBalance(Balance balance) {
        mBalance = balance;
    }

    public void setCoinRate(double coinRate) {
        mCoinRate = coinRate;
    }


    public void setTriggerRate(double triggerRate) {
        mTriggerRate = triggerRate;
    }

    public ArrayList<TradeInfo> getTradeInfoList() {
        ArrayList<TradeInfo> tradeInfoList = new ArrayList<>();

        double totalAsKrw = mBalance.getTotalAsKrw();

        double targetCoin = (totalAsKrw * mCoinRate) / mBalance.getCoinCount();

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

    private TradeInfo createBuyTradeInfo(Coin coin, double targetCoin) {
        double diff = targetCoin - coin.getSellKrw();
        double rate = (diff / targetCoin) * 100.f;

        TradeInfo tradeInfo = new TradeInfo(coin.getCoinType());

        if (rate > mTriggerRate) {
            tradeInfo.setTradeType(TradeInfo.TradeType.BUY);
            tradeInfo.setTradeCoinAmount(coin.getSellCoin(diff));
        }
        return tradeInfo;
    }

    private TradeInfo createSellTradeInfo(Coin coin, double targetCoin) {
        double diff = coin.getBuyKrw() - targetCoin;
        double rate = (diff / targetCoin) * 100.f;
        TradeInfo tradeInfo = new TradeInfo(coin.getCoinType());
        if (rate > mTriggerRate) {
            tradeInfo.setTradeType(TradeInfo.TradeType.SELL);
            tradeInfo.setTradeCoinAmount(coin.getBuyCoin(diff));
        }
        return tradeInfo;
    }

}
