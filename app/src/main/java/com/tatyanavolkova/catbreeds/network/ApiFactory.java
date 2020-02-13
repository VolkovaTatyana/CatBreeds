package com.tatyanavolkova.catbreeds.network;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.util.HashMap;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiFactory {
    private static final String HEADER_KEY = "x-api-key";
    private static final String API_KEY = "f5db704d-f99a-4829-a7b9-d1fd2a3cfee0";
    private static final String BASE_URL = "https://api.thecatapi.com/v1/";

    private static final ApiFactory apiFactory = new ApiFactory();
    private static Retrofit retrofit;

    public static String getApiKey() {
        return API_KEY;
    }

    public static String getHeaderKey() {
        return HEADER_KEY;
    }

    public static ApiFactory getInstance() {
        return apiFactory;
    }

    private ApiFactory() {
        retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(BASE_URL)
                .build();
    }

    public ApiService getApiService() {
        return retrofit.create(ApiService.class);
    }
}
