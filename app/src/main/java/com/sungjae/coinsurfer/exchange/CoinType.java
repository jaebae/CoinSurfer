package com.sungjae.coinsurfer.exchange;


public enum CoinType {
    BTC(0.001f),
    ETH(0.01f),
    DASH(0.01f),
    LTC(0.1f),
    ETC(0.1f),
    XRP(10.f),
    BCH(0.001f),
    XMR(0.01f),
    ZEC(0.001f),
    QTUM(0.1f),
    BTG(0.01f),
    EOS(1.0f);


    private float mTradeUnit;

    CoinType(float tradeUnit) {
        mTradeUnit = tradeUnit;
    }

    float getTradeUnit() {
        return mTradeUnit;
    }
}
