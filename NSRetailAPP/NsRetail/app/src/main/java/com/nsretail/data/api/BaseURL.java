package com.nsretail.data.api;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BaseURL {

    public static final String BASE_URL = "https://nsoftsol.com:6002";

    public static final String BASE_URL_API = BASE_URL + "/api/";

    private static Retrofit getBaseUrl() {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.NONE);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor).build();

        Retrofit retrofit = new Retrofit.Builder()
//                .client(getUnsafeOkHttpClient().build())
                .client(client)
                .baseUrl(BASE_URL_API)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit;
    }

    public static StatusAPI getStatusAPI() {
        return BaseURL.getBaseUrl().create(StatusAPI.class);
    }
}