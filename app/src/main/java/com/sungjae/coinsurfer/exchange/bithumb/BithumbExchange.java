package com.sungjae.coinsurfer.exchange.bithumb;

import com.sungjae.coinsurfer.exchange.Exchange;
import com.sungjae.coinsurfer.tradedata.Balance;
import com.sungjae.coinsurfer.tradedata.Coin;
import com.sungjae.coinsurfer.tradedata.TradeInfo;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.HashMap;

public class BithumbExchange implements Exchange {
    private Api_Client mApi = new Api_Client();

    public BithumbExchange() {
    }

    @Override
    public void setApiKey(String connectKey, String secretKey) {
        mApi.setApiKey(connectKey, secretKey);
    }

    @Override
    public void getMarketPrice(Balance balance) throws Exception {
        String response = callApi("/public/ticker/ALL", null, null);

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

        balance.setKrw(Double.parseDouble(getString("total_krw", dataPart)));
    }

    @Override
    public TradeInfo trade(TradeInfo tradeInfo) throws Exception {
        HashMap<String, String> param = new HashMap<>();

        param.put("units", tradeInfo.getTradeCoinAmount());
        param.put("currency", tradeInfo.getCoinName());

        String apiName = "/trade/market_buy/";
        if (tradeInfo.getTradeType() == TradeInfo.TradeType.SELL) {
            apiName = "/trade/market_sell/";
        }

        String data = callApi(apiName, null, param);
        System.out.println(data);

        return getTradeResult(data, tradeInfo);
    }

    public TradeInfo getTradeResult(String data, TradeInfo tradeInfo) {
        TradeInfo result = new TradeInfo(tradeInfo.getCoinType());
        result.setTradeType(tradeInfo.getTradeType());

        JSONArray jsonArray = getArrayValueObj(data);
        double unit = 0;
        double totalKrw = 0;

        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObject = (JSONObject) jsonArray.get(i);

            unit += Double.parseDouble(getString("units", jsonObject));
            totalKrw += Double.parseDouble(getString("total", jsonObject));
        }


        result.setTradeCoinAmount(unit);
        result.setTradeKrw(totalKrw);

        return result;
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
            value = "" + obj;
        } else if (obj instanceof Integer) {
            value = "" + obj;
        }
        return value;
    }
}
