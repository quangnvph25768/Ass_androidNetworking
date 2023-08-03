package com.example.assignment_quangnvph25768;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface APIService {

    @GET("planetary/apod")
    Call<List<ImageNasa>> getApodList(
            @Query("api_key") String apiKey,
            @Query("start_date") String date
    );

    @POST("/apod")
    Call<Void> uploadObj(@Body ImageNasa img);


}
