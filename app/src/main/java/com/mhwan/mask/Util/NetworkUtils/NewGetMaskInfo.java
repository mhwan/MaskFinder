package com.mhwan.mask.Util.NetworkUtils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mhwan.mask.Item.MaskStoreItem;
import com.mhwan.mask.Util.AppUtility;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public class NewGetMaskInfo {
    private static NewGetMaskInfo instance;
    private Retrofit retrofit;
    private ApiService apiService;
    private NewGetMaskInfo(){}

    public static NewGetMaskInfo getInstance(){
        if (instance == null)
            instance = new NewGetMaskInfo();
        return instance;
    }

    public Retrofit getRetrofitClient() {
        //If condition to ensure we don't create multiple retrofit instances in a single application
        if (retrofit == null) {
            //Defining the Retrofit using Builder
            retrofit = new Retrofit.Builder()
                    .baseUrl(AppUtility.UrlList.BASE_URL) //This is the only mandatory call on Builder object.
                    .addConverterFactory(GsonConverterFactory.create()) // Convertor library used to convert response into POJO
                    .build();
        }
        return retrofit;
    }

    public void getStorebyGeo(Map<String, String> params, Callback<MaskStoreItem> callback){
        Retrofit retrofit = getRetrofitClient();
        NewGetMaskInfo.ApiService apiService = retrofit.create(NewGetMaskInfo.ApiService.class);
        Call<MaskStoreItem> call = apiService.getStoreFromGeo(params);
        call.enqueue(callback);
    }

    public void getStorebyAddress(String addr, Callback<MaskStoreItem> callback){
        Retrofit retrofit = getRetrofitClient();
        NewGetMaskInfo.ApiService apiService = retrofit.create(NewGetMaskInfo.ApiService.class);
        Call<MaskStoreItem> call = apiService.getStoreFromAddress(addr);
        call.enqueue(callback);
    }

    public interface ApiService {
        @GET("storesByGeo/json")
        Call<MaskStoreItem> getStoreFromGeo(@QueryMap Map<String, String> params);

        @GET("storesByAddr/json")
        Call<MaskStoreItem> getStoreFromAddress(@Query("address") String address);

    }
}
