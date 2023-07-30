package com.example.assignment_quangnvph25768;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import java.io.ByteArrayOutputStream;
import java.net.URL;
import java.util.List;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.util.Base64;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;

import java.net.URL;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
public class MainActivity extends AppCompatActivity {
    private static final String GET_LIST_URL = "https://api.nasa.gov/planetary/apod?api_key=DEMO_KEY&date=2023-07-01";
    private static final String API_KEY = "DEMO_KEY";
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
                uploadToSever();
            }
        });
    }

    public void getData(){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        APIService apiInterface = retrofit.create(APIService.class);

        // Lấy danh sách các ảnh từ ngày 2023-06-01 đến ngày 2023-06-15

        new Thread(new Runnable() {
            @Override
            public void run() {
                Call<List<ImageNasa>> call1 = apiInterface.getApodList(API_KEY, "2023-06-01", "2023-06-15");
                call1.enqueue(new Callback<List<ImageNasa>>() {
                    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
                    @Override
                    public void onResponse(Call<List<ImageNasa>> call, Response<List<ImageNasa>> response) {
                        if (response.isSuccessful()) {
                            List<ImageNasa> apodList = response.body();
                            // Duyệt danh sách các ảnh và làm gì đó với các ảnh
                            for (ImageNasa apod : apodList) {

                                try {
                                    Log.d("NasaImageActivity", "Date: " + apod.getDate() + ", URL: " +apod.getUrl() +"title"+apod.getExplanation() );
                                } catch (Exception e) {
                                    throw new RuntimeException(e);
                                }

                            }
                        } else {
                            // Handle error
                        }
                    }


                    @Override
                    public void onFailure(Call<List<ImageNasa>> call, Throwable t) {
                        t.printStackTrace();
                        // Handle error
                    }
                });
            }
        }).start();


        // Lấy danh sách các ảnh từ ngày 2023-06-16 đến ngày 2023-06-30
        new Thread(new Runnable() {
            @Override
            public void run() {
                Call<List<ImageNasa>> call2 = apiInterface.getApodList(API_KEY, "2023-06-16", "2023-06-30");
                call2.enqueue(new Callback<List<ImageNasa>>() {
                    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
                    @Override
                    public void onResponse(Call<List<ImageNasa>> call, Response<List<ImageNasa>> response) {
                        if (response.isSuccessful()) {
                            List<ImageNasa> apodList = response.body();
                            // Duyệt danh sách các ảnh và làm gì đó với các ảnh
                            for (ImageNasa apod : apodList) {

                                try {
                                    Log.d("NasaImageActivity", "Date: " + apod.getDate() + ", URL: " +apod.getUrl() );
                                } catch (Exception e) {
                                    throw new RuntimeException(e);
                                }


                            }
                        } else {
                            // Handle error
                        }
                    }

                    @Override
                    public void onFailure(Call<List<ImageNasa>> call, Throwable t) {
                        t.printStackTrace();
                        // Handle error
                    }
                });
            }
        }).start();

    }


    public void uploadToSever(){
        ImageNasa apod =new ImageNasa();

        APIService apiService = RetrofitClient.getClient("http://localhost:3000/").create(APIService.class);
        Call<Void> call = apiService.uploadObj(apod);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Toast.makeText(MainActivity.this, "thanh cong", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(MainActivity.this, "that bai", Toast.LENGTH_SHORT).show();
            }
        });
    }



}