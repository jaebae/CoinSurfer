package com.sungjae.coinsurfer.exchange;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class CoinTest {
    private Coin mCoin;

    @Before
    public void setUp() throws Exception {
        mCoin = new Coin(0);
    }

    @Test
    public void krwShouldBeReturn() throws Exception {
        mCoin.setSellPrice(1.f);
        mCoin.setCoinValue(1.f);
        assertThat(mCoin.getSellKrw(), is(1.f));

        mCoin.setCoinValue(2.f);
        assertThat(mCoin.getSellKrw(), is(2.f));


        mCoin.setBuyPrice(2.f);
        mCoin.setCoinValue(2.f);
        assertThat(mCoin.getBuyKrw(), is(4.f));

        mCoin.setBuyPrice(0.5f);
        mCoin.setCoinValue(2.f);
        assertThat(mCoin.getBuyKrw(), is(1.f));
    }

    @Test
    public void getCoinNameShouldReturn() throws Exception {
        assertThat(mCoin.getCoinName(), is("BTC"));
    }

    @Test
    public void shouldReturnMinTradeUnit() throws Exception {
        //assertThat(mCoin.getMinTradeUnit("BTC"), is(0.001f));
    }

}