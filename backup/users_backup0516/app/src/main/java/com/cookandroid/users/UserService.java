package com.cookandroid.users;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface UserService {

    @POST("accounts/login/")
    Call<LoginResponse> loginUser(@Body LoginRequest loginRequest);


    @POST("accounts/create/")
    Call<RegisterResponse> registerUser(@Body RegisterRequest registerRequest);

    @GET
    Call<MsgModal> getMessage(@Url String url);
}
