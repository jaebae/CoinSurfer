package com.sungjae.coinsurfer.tradedata;

import android.support.annotation.NonNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;

import static com.sungjae.coinsurfer.tradedata.CoinType.BCH;
import static com.sungjae.coinsurfer.tradedata.CoinType.BTC;
import static com.sungjae.coinsurfer.tradedata.CoinType.BTG;
import static com.sungjae.coinsurfer.tradedata.CoinType.DASH;
import static com.sungjae.coinsurfer.tradedata.CoinType.EOS;
import static com.sungjae.coinsurfer.tradedata.CoinType.LTC;
import static com.sungjae.coinsurfer.tradedata.CoinType.XRP;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class BalanceTest {

    private Balance mBalance;

    @Before
    public void setUp() throws Exception {
        mBalance = Balance.getsInstance();
    }

    @After
    public void tearDown() throws Exception {
        mBalance.clearCoinInfo();
        mBalance.setKrw(0.);
    }

    @Test
    public void setKrwTest() throws Exception {
        mBalance.setKrw(100.);
        assertThat(mBalance.getKrw(), is(100.));
    }

    @Test
    public void updateCoinTest() throws Exception {
        Coin btc = createCoin(BTC);

        assertThat(mBalance.getCoinCount(), is(0));

        mBalance.updateCoin(btc);
        assertThat(mBalance.getCoinCount(), is(1));
        assertThat(mBalance.getCoin(0), is(btc));

        btc = createCoin(BTC);
        mBalance.updateCoin(btc);
        assertThat(mBalance.getCoinCount(), is(1));
        assertThat(mBalance.getCoin(0), is(btc));

        Coin bch = createCoin(BCH);
        mBalance.updateCoin(bch);
        assertThat(mBalance.getCoinCount(), is(2));
        assertThat(mBalance.getCoin(0), is(btc));
        assertThat(mBalance.getCoin(1), is(bch));

        bch = createCoin(BCH);
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

        Coin coin = createCoin(BTC);
        mBalance.updateCoin(coin);
        assertThat(mBalance.getTotalAsKrw(), is(11.));

        coin = createCoin(BCH);
        mBalance.updateCoin(coin);
        assertThat(mBalance.getTotalAsKrw(), is(12.));
    }

    @NonNull
    private Coin createCoin(CoinType type) {
        Coin coin = new Coin(type);
        coin.setCurPrice(1);
        coin.setCoinValue(1);
        return coin;
    }

    @Test
    public void updateSupportCoinTest() throws Exception {
        ArrayList<CoinType> typeList = new ArrayList<>();

        /*verify init items*/
        typeList.add(BTC);
        typeList.add(BCH);
        typeList.add(XRP);
        verifyCoinList(typeList);

        /*verify update same items*/
        verifyCoinList(typeList);


        /*verify items added*/
        typeList.add(LTC);
        typeList.add(DASH);
        typeList.add(EOS);
        verifyCoinList(typeList);


        /*verify items removed*/
        typeList.remove(BTC);
        typeList.remove(LTC);
        typeList.remove(EOS);
        verifyCoinList(typeList);

        /*verify items add & removed*/
        typeList.add(BTG);
        typeList.remove(DASH);
        verifyCoinList(typeList);
    }

    private void verifyCoinList(ArrayList<CoinType> typeList) {
        ArrayList<Coin> coinList;
        mBalance.updateSupportCoinType(typeList);
        coinList = mBalance.getCoinList();
        assertThat(mBalance.getCoinCount(), is(typeList.size()));
        for (CoinType type : typeList) {
            assertThat(checkCoinTypeExist(coinList, type), is(true));
        }
    }

    private boolean checkCoinTypeExist(ArrayList<Coin> coinList, CoinType type) {
        for (Coin coin : coinList) {
            if (coin.getCoinType() == type) {
                return true;
            }
        }
        return false;
    }
}