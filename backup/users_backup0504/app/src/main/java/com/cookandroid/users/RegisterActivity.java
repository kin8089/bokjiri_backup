package com.cookandroid.users;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    Button btn_register;
    EditText et_rid, et_remail, et_rpass1, et_rpass2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        btn_register = findViewById(R.id.btn_register);
        et_rid = findViewById(R.id.et_rid);
        et_remail = findViewById(R.id.et_remail);
        et_rpass1 = findViewById(R.id.et_rpass1);
        et_rpass2 = findViewById(R.id.et_rpass2);

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (TextUtils.isEmpty(et_rid.getText().toString()) || TextUtils.isEmpty(et_remail.getText().toString()) || TextUtils.isEmpty(et_rpass1.getText().toString()) || TextUtils.isEmpty(et_rpass2.getText().toString())) {

                    String message = "입력정보가 정확하지 않습니다";
                    Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_SHORT).show();

                } else {
                    RegisterRequest registerRequest = new RegisterRequest();
                    registerRequest.setUsername(et_rid.getText().toString());
                    registerRequest.setEmail(et_remail.getText().toString());
                    registerRequest.setPassword1(et_rpass1.getText().toString());
                    registerRequest.setPassword2(et_rpass2.getText().toString());
                    registerUser(registerRequest);
                }
            }
        });

    }

    public void registerUser(RegisterRequest registerRequest){
        Call<RegisterResponse> registerResponseCall = ApiClient.getService().registerUser(registerRequest);
        registerResponseCall.enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {

                if(response.isSuccessful()){

                    String message = "회원가입에 성공하였습니다";
                    Toast.makeText(RegisterActivity.this, message,Toast.LENGTH_SHORT).show();

                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                    finish();

                }else {

                    String message = "회원가입에 실패하였습니다";
                    Toast.makeText(RegisterActivity.this, message,Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                String message = t.getLocalizedMessage();
                Toast.makeText(RegisterActivity.this, message,Toast.LENGTH_SHORT).show();

            }
        });
    }
}