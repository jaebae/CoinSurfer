package com.sungjae.coinsurfer.tradedata;

import android.support.annotation.NonNull;

import java.util.ArrayList;

public class Balance {
    private ArrayList<Coin> mCoinList = new ArrayList<>();
    private double mKrw;

    public Balance() {

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


    public void updateCoin(@NonNull Coin coin) {
        int index = getIndex(coin);
        if (index >= 0) {
            mCoinList.remove(index);
            mCoinList.add(index, coin);
        } else {
            mCoinList.add(coin);
        }
    }

    private int getIndex(@NonNull Coin coin) {
        for (int i = 0; i < mCoinList.size(); i++) {
            Coin pre = mCoinList.get(i);
            if (pre.getCoinName().equals(coin.getCoinName())) {
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

    public double getKrw() {
        return mKrw;
    }

    public void setKrw(double krw) {
        mKrw = krw;
    }
}
