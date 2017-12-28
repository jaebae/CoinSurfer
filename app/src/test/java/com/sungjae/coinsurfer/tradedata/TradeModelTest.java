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
        Balance balance = Balance.getsInstance();
        balance.setKrw(20000.);

        for (int i = 0; i < 4; i++) {
            Coin coin = createCoin(i, 100., 100.);
            balance.updateCoin(coin);
        }

        mTradeModel.setBalance(balance);
        mTradeModel.setCoinRate(0.8);
        mTradeModel.setTriggerRate(2.);

        ArrayList<TradeInfo> tradeInfoList = mTradeModel.getTradeInfoList();
        assertThat(tradeInfoList.size(), is(4));
        for (int i = 0; i < tradeInfoList.size(); i++) {
            TradeInfo tradeInfo = tradeInfoList.get(i);
            assertThat(tradeInfo.getCoinName(), is(CoinType.getCoinType(i).toString()));
            assertThat(tradeInfo.getTradeType(), is(TradeInfo.TradeType.BUY));
            //assertThat(tradeInfo.getTradeCoinAmount(), is("40"));
        }


        for (int i = 0; i < 4; i++) {
            Coin coin = balance.getCoin(i);
            coin.setCoinValue(40.);
            balance.updateCoin(coin);
        }
        balance.setKrw(4000.);

        Coin coin = createCoin(0, 95., 95.);
        coin.setCoinValue(40.);
        balance.updateCoin(coin);

        tradeInfoList = mTradeModel.getTradeInfoList();

        TradeInfo tradeInfo = tradeInfoList.get(0);
        assertThat(tradeInfo.getCoinName(), is(CoinType.getCoinType(0).toString()));
        assertThat(tradeInfo.getTradeType(), is(TradeInfo.TradeType.BUY));
        assertThat(tradeInfo.getTradeCoinAmount(), is("1.684"));


    }

    @NonNull
    private Coin createCoin(int coinType, double buyPrice, double sellPrice) {
        Coin coin = new Coin(coinType);
        coin.setBuyPrice(buyPrice);
        coin.setSellPrice(sellPrice);
        coin.setCurPrice((buyPrice + sellPrice) / 2);
        return coin;
    }
}