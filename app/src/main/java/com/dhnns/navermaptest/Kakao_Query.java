package com.dhnns.navermaptest;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface Kakao_Query {
    @GET("/v2/local/search/keyword.json")
    @Headers("Content-Type:application/x-www-form-urlencoded; charset=utf-8")
    Call<Documents> listPlaces(@Query("query") String query, @Query("x") String Longitude, @Query("y") String Latitude, @Query("sort") String sort);
}