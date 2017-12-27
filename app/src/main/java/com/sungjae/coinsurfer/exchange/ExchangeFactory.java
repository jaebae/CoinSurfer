package com.sungjae.coinsurfer.exchange;

import com.sungjae.coinsurfer.exchange.bithumb.BithumbExchange;
import com.sungjae.coinsurfer.setting.TradeSetting;


public class ExchangeFactory {
    static public Exchange createBithumbExchange(TradeSetting tradeSetting) {
        return new BithumbExchange(tradeSetting);
    }
}
