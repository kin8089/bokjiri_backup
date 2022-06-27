package com.cookandroid.users;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface InitMyapi {
//    @FormUrlEncoded
//    @POST("/rest-auth/login/")
//    Call<LoginResponse> loginUser(@Field("username") String username, @Field("password") String password);

    @POST("/rest-auth/login/")
//    @POST("/accounts/login/")
    Call<LoginResponse> loginUser(@Body LoginRequest loginRequest);

    @POST("/rest-auth/registration/")
    Call<RegisterResponse> registerUser(@Body RegisterRequest registerRequest);

    @Multipart
    @POST("/rest-auth/password/change/")
    Call<PasswordResponse> changepassword (@Header("Authorization") String token,
                                           @Body PasswordRequest passwordRequest);

    @GET
//('')
    Call<MsgModal> getMessage(@Url String url);

    @GET("/rest-auth/user/")
    Call<UserResponse> getAllData();
}
