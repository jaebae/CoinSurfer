package com.sungjae.coinsurfer.exchange;

import com.sungjae.coinsurfer.exchange.bithumb.BithumbExchange;


public class ExchangeFactory {
    static public Exchange createBithumbExchange() {
        return new BithumbExchange();
    }
}
