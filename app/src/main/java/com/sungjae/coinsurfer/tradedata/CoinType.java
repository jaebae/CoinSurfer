package com.sungjae.coinsurfer.tradedata;


import java.util.ArrayList;

public enum CoinType {
    BTC(0.001),
    BCH(0.001),
    ETH(0.01),
    QTUM(0.1),
    DASH(0.01),
    LTC(0.1),
    ETC(0.1),
    XRP(10.),
    XMR(0.01),
    ZEC(0.001),
    BTG(0.01),
    EOS(1.0);

    private static ArrayList<CoinType> sCoinList = new ArrayList<>();

    static {
        CoinType coinTypeList[] = CoinType.values();
        for (CoinType coinType : coinTypeList) {
            sCoinList.add(coinType);
        }
    }


    private double mTradeUnit;

    CoinType(double tradeUnit) {
        mTradeUnit = tradeUnit;
    }

    public static int getCoinTypeSize() {
        return sCoinList.size();
    }

    public static CoinType getCoinType(int code) {
        return sCoinList.get(code);
    }

    double getTradeUnit() {
        return mTradeUnit;
    }


}
