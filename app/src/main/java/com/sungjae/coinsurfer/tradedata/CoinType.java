package com.sungjae.coinsurfer.tradedata;


import java.util.ArrayList;

public enum CoinType {
    BTC(0.001), BCH(0.001), ETH(0.01), QTUM(0.1), DASH(0.01),
    LTC(0.1), ETC(0.1), XRP(10.), XMR(0.01), ZEC(0.001),
    BTG(0.1), EOS(1.0), ICX(1.0), VEN(1.0), TRX(100.0),
    ELF(10), MITH(10), MCO(10), OMG(0.1), KNC(1.0),
    GNT(10.), HSR(1.0), ZIL(100.), ETHOS(1.0), PAY(1.0),
    WAX(10.), POWR(10.), LRC(10.), GTO(10.), STEEM(10.),
    STRAT(1.0), ZRX(1.0), REP(0.1), AE(1.0), XEM(10.),
    SNT(10.), ADA(10.);

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

    public double getTradeUnit() {
        return mTradeUnit;
    }


}
