package com.sungjae.coinsurfer.exchange;

import org.junit.Test;

import static com.sungjae.coinsurfer.exchange.CoinType.BCH;
import static com.sungjae.coinsurfer.exchange.CoinType.BTC;
import static com.sungjae.coinsurfer.exchange.CoinType.DASH;
import static com.sungjae.coinsurfer.exchange.CoinType.ETH;
import static com.sungjae.coinsurfer.exchange.CoinType.LTC;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;


public class CoinTypeTest {

    @Test
    public void propertyTest() throws Exception {
        assertThat(BTC.toString(), is("BTC"));
        assertThat(BTC.getTradeUnit(), is(0.001f));

        assertThat(ETH.toString(), is("ETH"));
        assertThat(ETH.getTradeUnit(), is(0.01f));

        assertThat(DASH.toString(), is("DASH"));
        assertThat(DASH.getTradeUnit(), is(0.01f));

        assertThat(LTC.toString(), is("LTC"));
        assertThat(LTC.getTradeUnit(), is(0.1f));

        assertThat(BCH.toString(), is("BCH"));
        assertThat(BCH.getTradeUnit(), is(0.001f));


    }
}