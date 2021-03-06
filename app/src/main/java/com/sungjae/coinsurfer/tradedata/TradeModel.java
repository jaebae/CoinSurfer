package com.sungjae.coinsurfer.tradedata;


import java.util.ArrayList;

public class TradeModel {

    private double mCoinRate;

    private double mTriggerRate;

    private Balance mBalance;

    private static TradeModel sInstance;

    private boolean mTradeCancelled = false;

    public static synchronized TradeModel getInstance() {
        if (sInstance == null) {
            sInstance = new TradeModel();
        }

        return sInstance;
    }

    private TradeModel() {
        mBalance = Balance.getsInstance();
    }

    public void setCoinRate(double coinRate) {
        mCoinRate = coinRate;
    }


    public void setTriggerRate(double triggerRate) {
        mTriggerRate = triggerRate;
    }

    public ArrayList<TradeInfo> getTradeInfoList() throws Exception{
        ArrayList<TradeInfo> tradeInfoList = new ArrayList<>();

        double targetCoin = getTargetCoinAsKrw();

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

    public double getTargetCoinAsKrw() {
        double totalAsKrw = mBalance.getTotalAsKrw();
        return (totalAsKrw * mCoinRate) / mBalance.getCoinCount();
    }

    private TradeInfo createBuyTradeInfo(Coin coin, double targetCoin) throws Exception {
        double diff = targetCoin - coin.getSellKrw();
        double rate = (diff / targetCoin) * 100.f;

        TradeInfo tradeInfo = new TradeInfo(coin.getCoinType());

        if (rate > mTriggerRate) {
            checkAbnormalCase(rate);
            tradeInfo.setTradeType(TradeInfo.TradeType.BUY);
            tradeInfo.setTradeCoinAmount(coin.getSellCoin(diff));
            mTradeCancelled = false;
        }
        return tradeInfo;
    }

    private void checkAbnormalCase(double rate) throws Exception {
        if (rate > mTriggerRate * 2) {
            if (!mTradeCancelled) {
                mTradeCancelled = true;
                throw new TooBigDiffRateException("TOO BIG DIFF RATE");
            }
        }
    }

    private TradeInfo createSellTradeInfo(Coin coin, double targetCoin) throws Exception {
        double diff = coin.getBuyKrw() - targetCoin;
        double rate = (diff / targetCoin) * 100.f;
        TradeInfo tradeInfo = new TradeInfo(coin.getCoinType());
        if (rate > mTriggerRate) {
            checkAbnormalCase(rate);
            tradeInfo.setTradeType(TradeInfo.TradeType.SELL);
            tradeInfo.setTradeCoinAmount(coin.getBuyCoin(diff));
            mTradeCancelled = false;
        }
        return tradeInfo;
    }

}
