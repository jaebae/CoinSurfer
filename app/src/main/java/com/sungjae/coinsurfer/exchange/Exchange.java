package com.sungjae.coinsurfer.exchange;

import com.sungjae.coinsurfer.tradedata.Balance;
import com.sungjae.coinsurfer.tradedata.TradeInfo;


public interface Exchange {
    void setApiKey(String connectKey, String secretKey);

    void getMarketPrice(Balance balance) throws Exception;

    void getBalance(Balance balance) throws Exception;

    TradeInfo trade(TradeInfo tradeInfo) throws Exception;
}
