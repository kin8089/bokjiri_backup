package com.cookandroid.users;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface UserService {
    @POST("accounts/login/")
    Call<LoginResponse> loginUser(@Body LoginRequest loginRequest);

    @POST("accounts/create/")
    Call<RegisterResponse> registerUser(@Body RegisterRequest registerRequest);

//    @Multipart
//    @POST("rest-auth/password/change/")
//    Call<PasswordResponse> changepassword4 (@Header("Authorization") String token,
//                                           @Part PasswordRequest passwordRequest);

//    @Multipart
//    @POST("rest-auth/password/change/")
//    Call<PasswordResponse> changepassword3 (@Header("Authorization") String token,
//                                           @Part("new_password1")String new_password1,
//                                           @Part("new_password2")String new_password2);
//
//    @FormUrlEncoded
//    @POST("rest-auth/password/change/")
//    Call<PasswordResponse> changepassword5 (@Header("Authorization") String token,
//                                            @Field("new_password1") String new_password1,
//                                            @Field("new_password2")String new_password2);

    @Headers({"Authorization: application/json"})
    @POST("rest-auth/password/change/")
    Call<PasswordResponse> changepassword (@Body PasswordRequest passwordRequest);

    @POST("rest-auth/password/change/")
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
}
