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
        mCoin = Coin.getInstance();

    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowExceptionNotSupportCoin() throws Exception {
        mCoin.getMinTradeUnit("TEST");
    }

    @Test
    public void shouldReturnMinTradeUnit() throws Exception {
        assertThat(mCoin.getMinTradeUnit("BTC"), is(0.001f));
    }

}