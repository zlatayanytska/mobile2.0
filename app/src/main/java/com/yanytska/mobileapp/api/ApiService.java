package com.yanytska.mobileapp.api;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.yanytska.mobileapp.data.model.Fare;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {
    @GET("/fares")
    Call<JsonArray> getFares();

    @GET("/fares/{fareId}")
    Call<JsonObject> getFareById(@Path("fareId") String fareId);

    @POST("/fares")
    Call<Fare> addFare(@Body Fare fare);
}
