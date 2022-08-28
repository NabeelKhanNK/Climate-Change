package com.nabeel.climatechange.RestAPIs;

import com.nabeel.climatechange.model.News;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ClimateChange_API {
    @GET("everything")
    Call<News> getNews(@Query("q") String q, @Query("apiKey") String apiKey);
}
