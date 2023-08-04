package com.example.assignment_quangnvph25768.API;

import com.example.assignment_quangnvph25768.model.ImageInfoRequest;
import com.example.assignment_quangnvph25768.model.ImageNasa;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface APIService {

    @GET("planetary/apod")
    Call<ImageNasa> getApodList(
            @Query("api_key") String apiKey,
            @Query("date") String date
    );

    @POST("/apod")
    Call<Void> uploadObj(@Body ImageNasa img);

    @GET("/get-apod")
    Call<List<ImageNasa>> getImageFormServer();

    @PUT("/update-apod/{id}")
    Call<ResponseBody> updateImage(@Path("id") String imageId, @Body ImageInfoRequest request);
    @DELETE("/delete-apod/{id}")
    Call<ResponseBody> deleteImage(@Path("id") String imageId);

}
