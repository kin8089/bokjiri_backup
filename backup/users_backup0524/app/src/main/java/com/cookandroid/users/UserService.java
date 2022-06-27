package com.cookandroid.users;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface UserService {
    @POST("rest-auth/login/")
    Call<LoginResponse> loginUser(@Body LoginRequest loginRequest);

    @POST("rest-auth/registration/")
    Call<RegisterResponse> registerUser(@Body RegisterRequest registerRequest);

//    @Multipart
//    @POST("rest-auth/password/change/")
//    Call<PasswordResponse> changepassword (@Header("Authorization") String token,
//                                           @Part MultipartBody.Part passwordRequest);

    @Multipart
    @POST("rest-auth/password/change/")
    Call<PasswordResponse> changepassword (@Header("Authorization") String token,
                                           @Part("new_password1")String new_password1,
                                           @Part("new_password2")String new_password2);

//    @GET("sibal/message/")
//    Call<MsgModal> getMessage(@Url String url);

    @POST("sibal/message/")
    Call<MsgModal> getMessage(@Body ChatRequest chatRequest);

    @POST("sibal/message/")
    Call<ChatResponse> chatbot(@Body ChatRequest chatRequest);
}
