package com.sungjae.coinsurfer.exchange;

import android.support.annotation.NonNull;

import java.util.ArrayList;

public class Balance {
    ArrayList<Coin> mCoin = new ArrayList<>();
    float mKrw;

    public Balance() {

    }

    public void updateCoin(@NonNull Coin coin) {
        int index = getIndex(coin);
        if (index >= 0) {
            mCoin.remove(index);
        }

        mCoin.add(coin);
    }

    private int getIndex(@NonNull Coin coin) {
        for (int i = 0; i < mCoin.size(); i++) {
            Coin pre = mCoin.get(i);
            if (pre.getCoinName().equals(coin.getCoinName())) {
                return i;
            }
        }
        return -1;
    }

    public int getCoinCount() {
        return mCoin.size();
    }

    public Coin getCoin(int index) {
        return mCoin.get(index);
    }
}
