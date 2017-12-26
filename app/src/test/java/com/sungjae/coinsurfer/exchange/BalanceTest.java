package com.sungjae.coinsurfer.exchange;

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
        mBalance = new Balance();
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

    @NonNull
    private Coin createCoin(String coinName) {
        Coin coin = mock(Coin.class);
        when(coin.getCoinName()).thenReturn(coinName);
        return coin;
    }
}