package com.cookandroid.users;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface UserService {

    @POST("accounts/login/")
    Call<LoginResponse> loginUser(@Body LoginRequest loginRequest);


    @POST("accounts/create/")
    Call<RegisterResponse> registerUser(@Body RegisterRequest registerRequest);
}
