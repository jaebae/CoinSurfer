package com.sungjae.coinsurfer.tradedata;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static com.sungjae.coinsurfer.tradedata.CoinType.BTC;
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

        mCoin.setCoinValue(1.f);
        mCoin.setCurPrice(0.2f);
        assertThat(mCoin.getCurKrw(), is(0.2f));

    }

    @Test
    public void getCoinNameShouldReturn() throws Exception {
        assertThat(mCoin.getCoinName(), is("BTC"));
        assertThat(mCoin.getCoinType(), is(BTC));
    }

    @Test
    public void getBuyKrwTest() throws Exception {
        mCoin.setBuyPrice(10.f);
        assertThat(mCoin.getBuyCoin(10.f), is(1.f));
        assertThat(mCoin.getBuyCoin(100.f), is(10.f));

        mCoin.setSellPrice(5.f);
        assertThat(mCoin.getSellCoin(10.f), is(2.f));
        assertThat(mCoin.getSellCoin(100.f), is(20.f));
    }

}