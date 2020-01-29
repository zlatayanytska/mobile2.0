package com.yanytska.mobileapp.api;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public final class RetrofitClient {

    private static ApiService service;

    //URL
    private static final String ROOT_URL = "https://back-end-266320.appspot.com/";

    public static ApiService getRetroClient() {
        if (service == null) {

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(ROOT_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(createDefaultOkHttpClient())
                    .build();

            service = retrofit.create(ApiService.class);
        }
        return service;
    }

    private static OkHttpClient createDefaultOkHttpClient() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return new OkHttpClient().newBuilder()
                .addInterceptor(interceptor)
                .build();
    }
}
