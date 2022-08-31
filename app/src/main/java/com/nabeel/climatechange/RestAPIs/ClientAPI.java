package com.nabeel.climatechange.RestAPIs;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ClientAPI {
    public  static final  String BASE_URL ="https://newsapi.org/v2/";
    public  static final  String BASE_URL2 ="https://air-quality.p.rapidapi.com/forecast/?lat=28.6460998&lon=77.2829082&hours=1";
    public static final String KEY = "d6fdffb53ae34c90ae721c0230764086";



    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.NONE);
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(60000, TimeUnit.SECONDS)
                .readTimeout(60000, TimeUnit.SECONDS)
                .addInterceptor(logging).build();

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build();

        return retrofit;
    }

    public static Retrofit getAQI() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.NONE);
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(60000, TimeUnit.SECONDS)
                .readTimeout(60000, TimeUnit.SECONDS)
                .addInterceptor(logging).build();

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL2)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build();

        return retrofit;
    }
}
