package com.teaminversion.envisionbuddy;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface API {

    String BASE_URL = "https://console.echoAR.xyz/";
    @GET("search?")

    Call<ArrayList<JSONProcessActivity>> getResult(@Query("key") String key, @Query("keywords") String keywords);
}