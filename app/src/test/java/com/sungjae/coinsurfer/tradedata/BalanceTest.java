package com.sungjae.coinsurfer.tradedata;

import android.support.annotation.NonNull;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BalanceTest {

    private Balance mBalance;

    @Before
    public void setUp() throws Exception {
        mBalance = Balance.getsInstance();
    }

    @Test
    public void setKrwTest() throws Exception {
        mBalance.setKrw(100.);
        assertThat(mBalance.getKrw(), is(100.));
    }

    @Test
    public void updateCoinTest() throws Exception {
        Coin btc = createCoin("BTC");

        assertThat(mBalance.getCoinCount(), is(0));

        mBalance.updateCoin(btc);
        assertThat(mBalance.getCoinCount(), is(1));
        assertThat(mBalance.getCoin(0), is(btc));

        btc = createCoin("BTC");
        mBalance.updateCoin(btc);
        assertThat(mBalance.getCoinCount(), is(1));
        assertThat(mBalance.getCoin(0), is(btc));

        Coin bch = createCoin("BCH");
        mBalance.updateCoin(bch);
        assertThat(mBalance.getCoinCount(), is(2));
        assertThat(mBalance.getCoin(0), is(btc));
        assertThat(mBalance.getCoin(1), is(bch));

        bch = createCoin("BCH");
        mBalance.updateCoin(bch);
        assertThat(mBalance.getCoinCount(), is(2));
        assertThat(mBalance.getCoin(0), is(btc));
        assertThat(mBalance.getCoin(1), is(bch));
    }

    @Test
    public void getTotalAsKrwTest() throws Exception {
        assertThat(mBalance.getTotalAsKrw(), is(0.));

        mBalance.setKrw(10.f);
        assertThat(mBalance.getTotalAsKrw(), is(10.));

        Coin coin = createCoin("BTC");
        mBalance.updateCoin(coin);
        assertThat(mBalance.getTotalAsKrw(), is(11.));

        coin = createCoin("BCH");
        mBalance.updateCoin(coin);
        assertThat(mBalance.getTotalAsKrw(), is(12.));
    }

    @NonNull
    private Coin createCoin(String coinName) {
        Coin coin = mock(Coin.class);
        when(coin.getCoinName()).thenReturn(coinName);
        when(coin.getCurKrw()).thenReturn(1.);
        return coin;
    }
}