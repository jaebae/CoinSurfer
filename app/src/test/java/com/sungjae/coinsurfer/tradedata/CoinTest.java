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
        mCoin.setSellPrice(1.);
        mCoin.setCoinValue(1.);
        assertThat(mCoin.getSellKrw(), is(1.));

        mCoin.setCoinValue(2.);
        assertThat(mCoin.getSellKrw(), is(2.));


        mCoin.setBuyPrice(2.);
        mCoin.setCoinValue(2.);
        assertThat(mCoin.getBuyKrw(), is(4.));

        mCoin.setBuyPrice(0.5);
        mCoin.setCoinValue(2.);
        assertThat(mCoin.getBuyKrw(), is(1.));

        mCoin.setCoinValue(1.);
        mCoin.setCurPrice(0.2);
        assertThat(mCoin.getCurKrw(), is(0.2));

    }

    @Test
    public void getCoinNameShouldReturn() throws Exception {
        assertThat(mCoin.getCoinName(), is("BTC"));
        assertThat(mCoin.getCoinType(), is(BTC));
    }

    @Test
    public void getBuyKrwTest() throws Exception {
        mCoin.setBuyPrice(10.);
        assertThat(mCoin.getBuyCoin(10.), is(1.));
        assertThat(mCoin.getBuyCoin(100.), is(10.));

        mCoin.setSellPrice(5.);
        assertThat(mCoin.getSellCoin(10.), is(2.));
        assertThat(mCoin.getSellCoin(100.), is(20.));
    }

    /*@Test
    public void makeAsTradeUnitTest() throws Exception {

        Coin coin = new Coin(BTC);
        assertThat(coin.makeAsTradeUnit(12.12345678), is("12.123"));

        coin = new Coin(ETH);
        assertThat(coin.makeAsTradeUnit(12.12345678), is("12.12"));

        coin = new Coin(QTUM);
        assertThat(coin.makeAsTradeUnit(12.12345678), is("12.1"));

        coin = new Coin(XRP);
        assertThat(coin.makeAsTradeUnit(12.12345678), is("10"));
        assertThat(coin.makeAsTradeUnit(19.12345678), is("20"));
    }*/
}