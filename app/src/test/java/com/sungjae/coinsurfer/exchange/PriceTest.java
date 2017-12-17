package com.sungjae.coinsurfer.exchange;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class PriceTest {
    Price mPrice = new Price();

    @Test
    public void krwShouldBeReturn() throws Exception {
        mPrice.setPrice(1.f);
        mPrice.setCoin(1.f);
        assertThat(mPrice.getKrw(), is(1.f));

        mPrice.setCoin(2.f);
        assertThat(mPrice.getKrw(), is(2.f));


        mPrice.setPrice(2.f);
        mPrice.setCoin(2.f);
        assertThat(mPrice.getKrw(), is(4.f));

        mPrice.setPrice(0.5f);
        mPrice.setCoin(2.f);
        assertThat(mPrice.getKrw(), is(1.f));
    }
}