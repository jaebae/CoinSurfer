package com.sungjae.coinsurfer.tradedata;

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

        ArrayList<TradeInfo> tradeInfoList = mTradeModel.getTradeInfoList();
        assertThat(tradeInfoList.size(), is(4));
        for (int i = 0; i < tradeInfoList.size(); i++) {
            TradeInfo tradeInfo = tradeInfoList.get(i);
            assertThat(tradeInfo.getCoinType().toString(), is(CoinType.getCoinType(i).toString()));
            assertThat(tradeInfo.getTradeType(), is(TradeInfo.TradeType.BUY));
            assertThat(tradeInfo.getTradeAmount(), is(40.f));
        }


        for (int i = 0; i < 4; i++) {
            Coin coin = balance.getCoin(i);
            coin.setCoinValue(40.f);
            balance.updateCoin(coin);
        }
        balance.setKrw(4000.f);

        Coin coin = createCoin(0, 95.f, 95.f);
        coin.setCoinValue(40.f);
        balance.updateCoin(coin);

        tradeInfoList = mTradeModel.getTradeInfoList();

        TradeInfo tradeInfo = tradeInfoList.get(0);
        assertThat(tradeInfo.getCoinType().toString(), is(CoinType.getCoinType(0).toString()));
        assertThat(tradeInfo.getTradeType(), is(TradeInfo.TradeType.BUY));
        assertThat(tradeInfo.getTradeAmount(), is(1.684f));


    }

    @NonNull
    private Coin createCoin(int coinType, float buyPrice, float sellPrice) {
        Coin coin = new Coin(coinType);
        coin.setBuyPrice(buyPrice);
        coin.setSellPrice(sellPrice);
        coin.setCurPrice((buyPrice + sellPrice) / 2);
        return coin;
    }
}