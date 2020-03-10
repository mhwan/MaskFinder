package com.mhwan.mask.Util.NetworkUtils;


import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mhwan.mask.Item.MaskStoreItem;
import com.mhwan.mask.Util.AppUtility;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;

import javax.net.ssl.HttpsURLConnection;

/**
 *
 * v1 위치기반으로 정보 얻기
 * v2 장소기반으로 얻기
 */
public class GetMaskInfo {
    private static GetMaskInfo instance;
    private GetMaskInfo(){
    }
    public static GetMaskInfo getInstance(){
        if (instance == null)
            instance = new GetMaskInfo();

        return instance;
    }

    public ArrayList getStoresByAddress(String address) {
        ArrayList<MaskStoreItem> arr = null;
        try {
            arr =  getGeoWork(AppUtility.UrlList.BASE_URL+getADDRParams(URLEncoder.encode(address, "UTF-8")));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return arr;
    }

    private ArrayList getGeoWork(String urls) {
        ArrayList<MaskStoreItem> result = null;
        try {
            URL url = new URL(urls);
            Log.d("url", urls);
            URLConnection connection = url.openConnection();

            HttpsURLConnection httpsURLConnection = (HttpsURLConnection) connection;
            httpsURLConnection.setRequestMethod("GET");
            httpsURLConnection.setUseCaches(false);
            httpsURLConnection.setDoInput(true);
            httpsURLConnection.setDoOutput(true);

            if (((HttpsURLConnection) connection).getResponseCode() == 200)
                Log.d("page", "Okay");
            else
                Log.e("page", "Error");

            JsonObject object = getStringToJson(readInputStreamToString(httpsURLConnection));
            Gson gson = new Gson();
            JsonArray array = object.get("stores").getAsJsonArray();
            MaskStoreItem[] arr = (MaskStoreItem [])(gson.fromJson(array, MaskStoreItem[].class));

            result = new ArrayList<>(Arrays.asList(arr));


        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    public ArrayList getStoresByGeo(String lat, String lng, String meters){
        return getGeoWork(AppUtility.UrlList.BASE_URL + getGEOParams(lat, lng, meters));
    }
    private String getGEOParams(String lat, String lng, String meters){
        return AppUtility.UrlList.BY_GEO_URL+"?lat="+lat+"&lng="+lng+"&m="+meters;
    }

    private String getADDRParams(String address){
        return AppUtility.UrlList.BY_ADDR_URL+"?address="+address;
    }

    public JsonObject getStringToJson(String string) {
        JsonObject result;
        result = new JsonParser().parse(string).getAsJsonObject();
        return result;
    }

    public String readInputStreamToString(HttpsURLConnection connection){
        String result = null;
        StringBuffer sb = new StringBuffer();
        InputStream is = null;

        try {
            is = new BufferedInputStream(connection.getErrorStream());
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String inputLine = "";
            while ((inputLine = br.readLine()) != null) {
                sb.append(inputLine);
            }
            result = sb.toString();
        } catch (Exception e) {
            System.out.print("error");
            e.printStackTrace();
            result = null;
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    System.out.print("error");
                }
            }
        }

        return result;
    }
}
