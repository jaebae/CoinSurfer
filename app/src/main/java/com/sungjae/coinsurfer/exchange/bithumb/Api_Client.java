package com.sungjae.coinsurfer.exchange.bithumb;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

@SuppressWarnings("unused")
public class Api_Client {
    private static final String DEFAULT_ENCODING = "UTF-8";
    private static final String HMAC_SHA512 = "HmacSHA512";
    protected String api_url = "https://api.bithumb.com";
    protected String api_key = "";
    protected String api_secret = "";

    public Api_Client() {
    }

    public static String encodeURIComponent(String s) {
        String result = null;

        try {
            result = URLEncoder.encode(s, "UTF-8").replaceAll("\\+", "%20").replaceAll("\\%21", "!")
                    .replaceAll("\\%27", "'").replaceAll("\\%28", "(").replaceAll("\\%29", ")").replaceAll("\\%26", "&")
                    .replaceAll("\\%3D", "=").replaceAll("\\%7E", "~");
        }

        // This exception should never occur.
        catch (UnsupportedEncodingException e) {
            result = s;
        }

        return result;
    }

    public static byte[] hmacSha512(String value, String key) {
        try {
            SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(DEFAULT_ENCODING), HMAC_SHA512);

            Mac mac = Mac.getInstance(HMAC_SHA512);
            mac.init(keySpec);

            final byte[] macData = mac.doFinal(value.getBytes());
            byte[] hex = new Hex().encode(macData);

            // return mac.doFinal(value.getBytes(DEFAULT_ENCODING));
            return hex;

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public static String asHex(byte[] bytes) {
        return new String(Base64.encodeBase64(bytes));
    }

    public void setApiKey(String api_key, String api_secret) {
        this.api_key = api_key;
        this.api_secret = api_secret;
    }

    /**
     * ������ �ð��� ns�� �����Ѵ�.(1/1,000,000,000 ��)
     *
     * @return int
     */
    private String usecTime() {
        return String.valueOf(System.currentTimeMillis());
    }

    private String request(String strHost, String strMemod, HashMap<String, String> rgParams,
                           HashMap<String, String> httpHeaders) {
        String response = "";

        // SSL ����
        if (strHost.startsWith("https://")) {
            HttpRequest request = HttpRequest.get(strHost);
            // Accept all certificates
            request.trustAllCerts();
            // Accept all hostnames
            request.trustAllHosts();
        }

        if (strMemod.toUpperCase().equals("HEAD")) {
        } else {
            HttpRequest request = null;

            // POST/GET ����
            if (strMemod.toUpperCase().equals("POST")) {

                request = new HttpRequest(strHost, "POST");
                request.readTimeout(10000);

                if (httpHeaders != null && !httpHeaders.isEmpty()) {
                    httpHeaders.put("api-client-type", "2");
                    request.headers(httpHeaders);
                }
                if (rgParams != null && !rgParams.isEmpty()) {
                    request.form(rgParams);
                }
            } else {
                request = HttpRequest.get(strHost + Util.mapToQueryString(rgParams));
                request.readTimeout(10000);
            }


            response = request.body();
            request.disconnect();
        }

        return response;
    }

    private HashMap<String, String> getHttpHeaders(String endpoint, HashMap<String, String> rgData, String apiKey,
                                                   String apiSecret) {

        String strData = Util.mapToQueryString(rgData).replace("?", "");
        String nNonce = usecTime();

        strData = strData.substring(0, strData.length() - 1);
        strData = encodeURIComponent(strData);

        HashMap<String, String> hashMap = new HashMap<String, String>();

        String str = endpoint + ";" + strData + ";" + nNonce;

        String encoded = asHex(hmacSha512(str, apiSecret));

        hashMap.put("Api-Key", apiKey);
        hashMap.put("Api-Sign", encoded);
        hashMap.put("Api-Nonce", String.valueOf(nNonce));

        return hashMap;

    }

    @SuppressWarnings("unchecked")
    public String callApi(String endpoint, HashMap<String, String> params) throws Exception {
        HashMap<String, String> rgParams = new HashMap<String, String>();
        rgParams.put("endpoint", endpoint);

        if (params != null) {
            rgParams.putAll(params);
        }

        String api_host = api_url + endpoint;
        HashMap<String, String> httpHeaders = getHttpHeaders(endpoint, rgParams, api_key, api_secret);

        String rgResultDecode = request(api_host, "POST", rgParams, httpHeaders);
        // json �Ľ�
        HashMap<String, String> result;

        try {
            result = new ObjectMapper().readValue(rgResultDecode, HashMap.class);
            Object status = result.get("status");

            if (!status.toString().equals("0000")) {
                throw new Exception(result.get("message"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return rgResultDecode;
    }
}
