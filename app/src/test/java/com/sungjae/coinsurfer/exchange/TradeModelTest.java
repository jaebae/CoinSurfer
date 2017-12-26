package com.sungjae.coinsurfer.exchange;

import android.support.annotation.NonNull;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;


public class TradeModelTest {

    private TradeModel mTradeModel;

    @Before
    public void setUp() throws Exception {
        mTradeModel = new TradeModel();
    }

    @Test
    public void shouldTrade() throws Exception {
        Balance balance = new Balance();
        balance.setKrw(20000.f);

        for (int i = 0; i < 4; i++) {
            Coin coin = createCoin(i, 100.f, 100.f);
            balance.updateCoin(coin);
        }

        mTradeModel.setBalance(balance);
        mTradeModel.setCoinRate(0.8f);
        mTradeModel.setKrwRate(0.2f);
        mTradeModel.setTriggerRate(2.f);

        ArrayList<TradeInfo> mTradeInfoList = mTradeModel.getTradeInfoList();
        assertThat(mTradeInfoList.size(), is(4));
        for (int i = 0; i < mTradeInfoList.size(); i++) {
            TradeInfo tradeInfo = mTradeInfoList.get(i);
            assertThat(tradeInfo.getCoinType().toString(), is(CoinType.getCoinType(i).toString()));
            assertThat(tradeInfo.getTradeType(), is(TradeInfo.TradeType.BUY));
            assertThat(tradeInfo.getTradeAmount(), is(25.f));
        }
    }

    @NonNull
    private Coin createCoin(int coinType, float buyPrice, float sellPrice) {
        Coin coin = new Coin(coinType);
        coin.setBuyPrice(buyPrice);
        coin.setSellPrice(sellPrice);
        return coin;
    }
}