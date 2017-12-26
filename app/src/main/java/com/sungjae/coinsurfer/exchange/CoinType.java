package com.sungjae.coinsurfer.exchange;


import java.util.ArrayList;

public enum CoinType {
    BTC(0.001f),
    BCH(0.001f),
    ETH(0.01f),
    QTUM(0.1f),
    DASH(0.01f),
    LTC(0.1f),
    ETC(0.1f),
    XRP(10.f),
    XMR(0.01f),
    ZEC(0.001f),
    BTG(0.01f),
    EOS(1.0f);

    private static ArrayList<CoinType> sCoinList = new ArrayList<>();

    static {
        CoinType coinTypeList[] = CoinType.values();
        for (CoinType coinType : coinTypeList) {
            sCoinList.add(coinType);
        }
    }


    private float mTradeUnit;

    CoinType(float tradeUnit) {
        mTradeUnit = tradeUnit;
    }

    public static int getCoinTypeSize() {
        return sCoinList.size();
    }

    public static CoinType getCoinType(int code) {
        return sCoinList.get(code);
    }

    float getTradeUnit() {
        return mTradeUnit;
    }


}
