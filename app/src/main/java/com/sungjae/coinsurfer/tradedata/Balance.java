package com.sungjae.coinsurfer.tradedata;

import android.support.annotation.NonNull;

import java.util.ArrayList;

public class Balance {
    private static Balance sInstance;
    private ArrayList<Coin> mCoinList = new ArrayList<>();
    private double mKrw;

    private Balance() {

    }

    public synchronized static Balance getsInstance() {
        if (sInstance == null) {
            sInstance = new Balance();
        }
        return sInstance;
    }

    public double getTotalAsKrw() {
        double ret = mKrw;

        for (Coin coin : mCoinList) {
            ret += coin.getCurKrw();
        }

        return ret;
    }

    public void clearCoinInfo() {
        mCoinList.clear();
    }


    public synchronized void updateCoin(@NonNull Coin coin) {
        int index = getIndex(coin.getCoinType());
        if (index >= 0) {
            mCoinList.remove(index);
            mCoinList.add(index, coin);
        } else {
            mCoinList.add(coin);
        }
    }

    public int getIndex(@NonNull CoinType coinType) {
        for (int i = 0; i < mCoinList.size(); i++) {
            Coin pre = mCoinList.get(i);
            if (pre.getCoinType().equals(coinType)) {
                return i;
            }
        }
        return -1;
    }

    public int getCoinCount() {
        return mCoinList.size();
    }

    public Coin getCoin(int index) {
        return mCoinList.get(index);
    }

    public ArrayList<Coin> getCoinList() {
        return mCoinList;
    }

    public double getKrw() {
        return mKrw;
    }

    public void setKrw(double krw) {
        mKrw = krw;
    }
}
