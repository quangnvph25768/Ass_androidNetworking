package com.example.assignment_quangnvph25768.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import java.nio.charset.Charset;
import java.util.List;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.util.Base64;
import android.widget.Toast;

import com.example.assignment_quangnvph25768.API.APIService;
import com.example.assignment_quangnvph25768.API.RetrofitClient;
import com.example.assignment_quangnvph25768.R;
import com.example.assignment_quangnvph25768.model.ImageNasa;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
public class MainActivity extends AppCompatActivity {
    private static final String API_KEY = "KbecYPt80KFOs1SajTAfXlaKiL3KCgB7An2EFnxV";
    private static final String BASE_URL = "https://api.nasa.gov/";
    Button btn1, btn2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn1 = findViewById(R.id.btn_count);
        btn2 = findViewById(R.id.btn_count2);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    getData();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, ListImageActivity.class);
                startActivity(i);
            }
        });

    }

    public void getData() {
        ExecutorService executorService;
        executorService = Executors.newFixedThreadPool(2);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        APIService apiInterface = retrofit.create(APIService.class);
        for (int i = 1; i <= 10; i++) {
            final String dayOfMonth = String.valueOf(i);
            executorService.execute(new Runnable() {
                    @Override
                    public void run() {
                        String date = null;

                            date = "2023-07-" + dayOfMonth;
                        Log.d("DATE", "run: "+date);
                        Call<ImageNasa> call1 = apiInterface.getApodList(API_KEY, date);
                        call1.enqueue(new Callback<ImageNasa>() {
                            @Override
                            public void onResponse(Call<ImageNasa> call, Response<ImageNasa> response) {
                                if (response.isSuccessful()) {
                                    ImageNasa apodList = response.body();

                                    // Duyệt danh sách các ảnh và làm gì đó với các ảnh
//
                                    if(apodList!=null){

                                        try {
                                            String encodedUrl = apacheEncode(apodList.getUrl());
                                            apodList.setUrl(encodedUrl);
                                            uploadToSever(apodList);
                                            Log.d("NasaImageActivity", "Date: " + apodList.getDate() + ", URL: " + apacheEncode(apodList.getUrl()) + "title" + apodList.getCopyright());


                                        } catch (Exception e) {
                                            throw new RuntimeException(e);
                                        }
                                    }

                                }
                            }

                            @Override
                            public void onFailure(Call<ImageNasa> call, Throwable t) {

                            }

                        });
                        }
                });
            executorService.shutdown();
        }
    }

    public void uploadToSever(ImageNasa apod) {

        APIService apiService = RetrofitClient.getClient("http://192.168.1.23:3000").create(APIService.class);
        Call<Void> call = apiService.uploadObj(apod);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(MainActivity.this, "thanh cong", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(MainActivity.this, "that bai", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });
    }

    String apacheEncode(String decodedStr) {
        byte[] decodedByteArr = decodedStr.getBytes(Charset.forName("UTF-8"));
        return Base64.encodeToString(decodedByteArr, 1);
    }


}