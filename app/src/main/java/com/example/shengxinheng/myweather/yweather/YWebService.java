package com.example.shengxinheng.myweather.yweather;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

public interface YWebService {
    @GET("v1/public/yql")
    Call<ResponseBody> query(@QueryMap Map<String, String> options);
}
