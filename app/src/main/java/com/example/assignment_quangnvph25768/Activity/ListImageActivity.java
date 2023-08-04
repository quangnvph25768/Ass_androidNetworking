package com.example.assignment_quangnvph25768.Activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.example.assignment_quangnvph25768.API.APIService;
import com.example.assignment_quangnvph25768.API.RetrofitClient;
import com.example.assignment_quangnvph25768.R;
import com.example.assignment_quangnvph25768.model.ImageInfoRequest;
import com.example.assignment_quangnvph25768.model.ImageNasa;
import com.squareup.picasso.Picasso;

import java.nio.charset.Charset;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListImageActivity extends AppCompatActivity {

    private LinearLayout imageContainer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_image);
        imageContainer = findViewById(R.id.imageContainer);
        getImageFormServer();
    }

    public void getImageFormServer(){
        APIService apiService = RetrofitClient.getClient("http://192.168.1.23:3000").create(APIService.class);
        Call<List<ImageNasa>> call = apiService.getImageFormServer();
        call.enqueue(new Callback<List<ImageNasa>>() {
            @Override
            public void onResponse(Call<List<ImageNasa>> call, Response<List<ImageNasa>> response) {
                if(response.isSuccessful()){
                    List<ImageNasa> img = response.body();


                        if (img != null){
                            for (ImageNasa imageNasa : img){
                                ImageView imageView = new ImageView(ListImageActivity.this);
                                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                imageView.setLayoutParams(layoutParams);

                                // Sử dụng thư viện Picasso hoặc Glide để tải và hiển thị ảnh
                                Picasso.get().load(apacheEncode(imageNasa.getUrl())).into(imageView);

                                // Thêm ImageView vào LinearLayout
                                imageContainer.addView(imageView);

                                imageView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        showEditDeleteDialog(imageNasa);
                                    }
                                });
                            }
                        }


                    Toast.makeText(ListImageActivity.this, "thanh cong", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(ListImageActivity.this, "that bai", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<ImageNasa>> call, Throwable t) {

            }
        });
    }

    public void showEditDeleteDialog(ImageNasa imageNasa) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Edit/Delete Image");

        View dialogLayout = LayoutInflater.from(this).inflate(R.layout.edit_delete_dialog,null);
        ScrollView scrollView = new ScrollView(this);
        scrollView.addView(dialogLayout);

        builder.setView(scrollView);

        EditText edtTitle = dialogLayout.findViewById(R.id.editTextTitle);
        EditText editCopyright = dialogLayout.findViewById(R.id.editTextCopyright);
        EditText edtExplanation = dialogLayout.findViewById(R.id.editTextExplanation);
        Button buttonEdit = dialogLayout.findViewById(R.id.buttonEdit);
        Button buttonDelete = dialogLayout.findViewById(R.id.buttonDelete);

        edtTitle.setText(imageNasa.getTitle());
        edtExplanation.setText(imageNasa.getExplanation());
        editCopyright.setText(imageNasa.getCopyright());
        AlertDialog alertDialog = builder.create();
        buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newTitle = edtTitle.getText().toString();
                String newExplanation = edtExplanation.getText().toString();
                String newCopyright = editCopyright.getText().toString();
                updateImageInfo(imageNasa.getId(), newTitle, newExplanation, newCopyright);

                alertDialog.dismiss();
            }

        });

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ListImageActivity.this);
                builder.setTitle("Confirm Delete")
                                .setMessage("Bạn có muốn xóa ảnh này không?")
                                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                deleteImage(imageNasa.getId());
                                                dialogInterface.dismiss();
                                                getImageFormServer();
                                                alertDialog.dismiss();
                                            }
                                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        }).show();


            }
        });

        alertDialog.show();
    }

    private void updateImageInfo(String imageId, String newTitle, String explanation, String copyright) {
        APIService apiService = RetrofitClient.getClient(" http://192.168.1.23:3000").create(APIService.class);
        ImageInfoRequest request = new ImageInfoRequest(newTitle, explanation,copyright);
        Call<ResponseBody> call = apiService.updateImage(imageId, request);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    // Xử lý khi cập nhật thành công
                    Toast.makeText(ListImageActivity.this, "Thông tin đã được cập nhật", Toast.LENGTH_SHORT).show();
                    getImageFormServer();
                } else {
                    // Xử lý khi cập nhật thất bại
                    Toast.makeText(ListImageActivity.this, "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // Xử lý khi có lỗi xảy ra
                Toast.makeText(ListImageActivity.this, "Có lỗi xảy ra", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteImage(String imageId) {
        APIService apiService = RetrofitClient.getClient(" http://192.168.1.23:3000").create(APIService.class);
        Call<ResponseBody> call = apiService.deleteImage(imageId);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    // Xử lý khi xóa thành công
                    Toast.makeText(ListImageActivity.this, "Ảnh đã được xóa", Toast.LENGTH_SHORT).show();
                } else {
                    // Xử lý khi xóa thất bại
                    Toast.makeText(ListImageActivity.this, "Xóa thất bại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // Xử lý khi có lỗi xảy ra
                Toast.makeText(ListImageActivity.this, "Có lỗi xảy ra", Toast.LENGTH_SHORT).show();
            }
        });

    }
    String apacheEncode(String encode) {
        byte[] decodedByteArr = Base64.decode(encode,Base64.DEFAULT);
        return new String(decodedByteArr,Charset.forName("UTF-8"));
    }
}