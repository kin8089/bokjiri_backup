package com.cookandroid.users;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Url;

public interface UserService {
    @POST("accounts/login/")
    Call<LoginResponse> loginUser(@Body LoginRequest loginRequest);

    @POST("accounts/create/")
    Call<RegisterResponse> registerUser(@Body RegisterRequest registerRequest);

    @PUT("/accounts/password_change/")
    Call<PasswordResponse> changepassword (@Body PasswordRequest passwordRequest);

    @PUT("/accounts/password_change/")
    Call<PasswordResponse> changepassword2 (@Header ("Authorization") String key, @Body PasswordRequest passwordRequest);

//    @GET("sibal/message/")
//    Call<MsgModal> getMessage(@Url String url);

    @POST("chatting/chat/")
    Call<MsgModal> getMessage(@Body ChatRequest chatRequest);

    @POST("chatting/chat/")
    Call<ChatResponse> chatbot(@Body ChatRequest chatRequest);

    @POST("chatting/chat/")
    Call<ChatResponse> chatbot2(@Header ("Authorization") String key, @Body ChatRequest chatRequest);

    @FormUrlEncoded
    @POST("chatting/chat/")
    Call<ChatResponse> chatbot3(@Field("my_chat")String my_chat);

    @FormUrlEncoded
    @POST("chatting/chat/")
    Call<ChatResponse> chatbot4(@Header ("Authorization") String key, @Field("my_chat")String my_chat);

    @GET("/accounts/user/")
    Call<UserResponse> user(@Url String url);
}
