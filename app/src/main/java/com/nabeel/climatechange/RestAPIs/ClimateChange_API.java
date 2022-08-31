package com.nabeel.climatechange.RestAPIs;

import com.nabeel.climatechange.model.AirQualityIndex;
import com.nabeel.climatechange.model.News;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface ClimateChange_API {
    @GET("everything")
    Call<News> getNews(@Query("q") String q, @Query("language") String language , @Query("apiKey") String apiKey);

    @Headers({"X-RapidAPI-Key: aacbffc0a7mshf847476264ff61ep1a5b75jsn7660dd782e08",
    "X-RapidAPI-Host: air-quality.p.rapidapi.com"})
    @GET("airquality")
    Call<AirQualityIndex> getAqi(@Query("lat") String lat, @Query("lon") String lon, @Query("hours") String hours);


//    @Part("X-RapidAPI-Key") String key,@Part("X-RapidAPI-Host") String host
}
