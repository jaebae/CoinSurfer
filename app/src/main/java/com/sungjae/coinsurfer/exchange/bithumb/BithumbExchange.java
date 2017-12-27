package com.sungjae.coinsurfer.exchange.bithumb;

import com.sungjae.coinsurfer.exchange.Exchange;
import com.sungjae.coinsurfer.setting.TradeSetting;
import com.sungjae.coinsurfer.tradedata.Balance;
import com.sungjae.coinsurfer.tradedata.Coin;
import com.sungjae.coinsurfer.tradedata.CoinType;
import com.sungjae.coinsurfer.tradedata.TradeInfo;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by 배성재 on 2017-12-27.
 */

public class BithumbExchange implements Exchange {
    private TradeSetting mTradeSetting;
    private Api_Client mApi = new Api_Client();

    public BithumbExchange(TradeSetting tradeSetting) {
        mTradeSetting = tradeSetting;
    }

    @Override
    public void setApiKey(String connectKey, String secretKey) {
        mApi.setApiKey(connectKey, secretKey);
    }

    @Override
    public void getMarketPrice(Balance balance) throws Exception {
        ArrayList<Coin> ret = new ArrayList<>();

        String response = callApi("/public/ticker/ALL", null, null);
        ArrayList<CoinType> coinTypeList = mTradeSetting.getEnableCoinList();

        JSONObject jsonResult = (JSONObject) new JSONParser().parse(response);
        JSONObject dataPart = (JSONObject) jsonResult.get("data");

        int cnt = balance.getCoinCount();

        for (int i = 0; i < cnt; i++) {
            Coin coin = balance.getCoin(i);
            JSONObject jsonCoinPrice = (JSONObject) dataPart.get(coin.getCoinName());

            coin.setBuyPrice(Double.parseDouble(getString("buy_price", jsonCoinPrice)));
            coin.setSellPrice(Double.parseDouble(getString("sell_price", jsonCoinPrice)));
            coin.setCurPrice(Double.parseDouble(getString("closing_price", jsonCoinPrice)));
            balance.updateCoin(coin);
        }
    }

    @Override
    public void getBalance(Balance balance) throws Exception {
        HashMap<String, String> param = new HashMap<>();
        param.put("currency", "ALL");

        String response = callApi("/info/balance/ALL", null, param);

        JSONObject jsonResult = (JSONObject) new JSONParser().parse(response);
        JSONObject dataPart = (JSONObject) jsonResult.get("data");

        int cnt = balance.getCoinCount();

        for (int i = 0; i < cnt; i++) {
            Coin coin = balance.getCoin(i);

            String keyName = "total_" + coin.getCoinName().toLowerCase();

            double count = Double.parseDouble(getString(keyName, dataPart));
            coin.setCoinValue(count);
            balance.updateCoin(coin);
        }
    }

    @Override
    public ArrayList<TradeInfo> buy(CoinType coinType, double amount) throws Exception {
        return null;
    }

    @Override
    public ArrayList<TradeInfo> sell(CoinType coinType, double amount) throws Exception {
        return null;
    }


    private String callApi(String api, String urlParam, HashMap<String, String> param) throws Exception {
        if (urlParam != null && !urlParam.isEmpty()) {
            api += urlParam;
        }

        return mApi.callApi(api, param);
    }

    private JSONArray getArrayValueObj(String json) {
        JSONArray ret = null;
        try {
            JSONObject jsonObj = (JSONObject) new JSONParser().parse(json);
            ret = (JSONArray) jsonObj.get("data");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return ret;

    }

    private String getString(String key, JSONObject data) {
        String value = "";
        Object obj = data.get(key);
        if (obj instanceof String) {
            value = (String) obj;
        } else if (obj instanceof Long) {
            value = "" + (Long) obj;
        } else if (obj instanceof Integer) {
            value = "" + obj;
        }
        return value;
    }
}
