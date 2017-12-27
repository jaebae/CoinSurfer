package com.sungjae.coinsurfer.exchange;

import com.sungjae.coinsurfer.tradedata.Balance;
import com.sungjae.coinsurfer.tradedata.CoinType;
import com.sungjae.coinsurfer.tradedata.TradeInfo;

import java.util.ArrayList;


public interface Exchange {
    void setApiKey(String connectKey, String secretKey);

    void getMarketPrice(Balance balance) throws Exception;

    void getBalance(Balance balance) throws Exception;

    ArrayList<TradeInfo> buy(CoinType coinType, double amount) throws Exception;

    ArrayList<TradeInfo> sell(CoinType coinType, double amount) throws Exception;
}
