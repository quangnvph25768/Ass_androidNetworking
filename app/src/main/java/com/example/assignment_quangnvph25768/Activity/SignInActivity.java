package com.example.assignment_quangnvph25768.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.assignment_quangnvph25768.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignInActivity extends AppCompatActivity {
    EditText ed_email, ed_passWord;
    Button btn_register;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        mAuth = FirebaseAuth.getInstance();
        ed_email = findViewById(R.id.edtEamilRG);
        ed_passWord = findViewById(R.id.edtPassRG);

        btn_register =findViewById(R.id.btnRegister);

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register();
            }
        });

    }

    private void register() {

        String email, passWord;
        email = ed_email.getText().toString();
        passWord = ed_passWord.getText().toString();

        if(TextUtils.isEmpty(email)|| TextUtils.isEmpty(passWord)){
            Toast.makeText(this, "Nhập đủ thông tin", Toast.LENGTH_SHORT).show();
        }
        mAuth.createUserWithEmailAndPassword(email,passWord).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(SignInActivity.this, "Đăng kí thành công", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(SignInActivity.this, LoginActivity.class);
                    startActivity(i);
                }else{
                    Toast.makeText(SignInActivity.this, "Đăng kí thất bại", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}