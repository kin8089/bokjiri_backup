package com.cookandroid.users;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface UserService {
    @POST("rest-auth/login/")
    Call<LoginResponse> loginUser(@Body LoginRequest loginRequest);

    @POST("rest-auth/registration/")
    Call<RegisterResponse> registerUser(@Body RegisterRequest registerRequest);

    @POST("rest-auth/password/change/")
    Call<PasswordResponse> changepassword (@Body PasswordRequest passwordRequest);

    @GET//('')
    Call<MsgModal> getMessage(@Url String url);
}
