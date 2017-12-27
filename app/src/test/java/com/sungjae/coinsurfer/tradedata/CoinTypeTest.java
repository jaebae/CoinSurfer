package com.sungjae.coinsurfer.tradedata;

import org.junit.Test;

import static com.sungjae.coinsurfer.tradedata.CoinType.BCH;
import static com.sungjae.coinsurfer.tradedata.CoinType.BTC;
import static com.sungjae.coinsurfer.tradedata.CoinType.BTG;
import static com.sungjae.coinsurfer.tradedata.CoinType.DASH;
import static com.sungjae.coinsurfer.tradedata.CoinType.EOS;
import static com.sungjae.coinsurfer.tradedata.CoinType.ETC;
import static com.sungjae.coinsurfer.tradedata.CoinType.ETH;
import static com.sungjae.coinsurfer.tradedata.CoinType.LTC;
import static com.sungjae.coinsurfer.tradedata.CoinType.QTUM;
import static com.sungjae.coinsurfer.tradedata.CoinType.XMR;
import static com.sungjae.coinsurfer.tradedata.CoinType.XRP;
import static com.sungjae.coinsurfer.tradedata.CoinType.ZEC;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;


public class CoinTypeTest {

    @Test
    public void propertyTest() throws Exception {
        assertThat(BTC.toString(), is("BTC"));
        assertThat(BTC.getTradeUnit(), is(0.001));

        assertThat(ETH.toString(), is("ETH"));
        assertThat(ETH.getTradeUnit(), is(0.01));

        assertThat(DASH.toString(), is("DASH"));
        assertThat(DASH.getTradeUnit(), is(0.01));

        assertThat(LTC.toString(), is("LTC"));
        assertThat(LTC.getTradeUnit(), is(0.1));

        assertThat(BCH.toString(), is("BCH"));
        assertThat(BCH.getTradeUnit(), is(0.001));
    }

    @Test
    public void getCoinTypeTest() throws Exception {
        CoinType typeList[] = {BTC, BCH, ETH, QTUM, DASH, LTC, ETC, XRP, XMR, ZEC, BTG, EOS};

        int i = 0;

        assertThat(CoinType.getCoinTypeSize(), is(typeList.length));
        for (CoinType type : typeList) {
            assertThat(CoinType.getCoinType(i++), is(type));
        }
    }


}