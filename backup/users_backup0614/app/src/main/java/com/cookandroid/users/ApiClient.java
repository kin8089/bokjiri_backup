package com.cookandroid.users;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static ApiClient instance = null;
    private static String authToken;

    //  로그인 할때 호출 -> 토큰 발급 전
    public static Retrofit getRetrofit() {

        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder().addInterceptor(httpLoggingInterceptor).build();

        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
//                .baseUrl("http://43.200.22.195:8000/")
                .baseUrl("http://192.168.0.18:8000/")
                .client(okHttpClient)
                .build();

        return retrofit;

    }

    public static Retrofit getRetrofit2(String authToken) {

        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder().addInterceptor(httpLoggingInterceptor).build();

        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
//                .baseUrl("http://43.200.22.195:8000/")
                .baseUrl("http://192.168.0.18:8000/")
                .client(okHttpClient)
                .build();

        return retrofit;
    }

    //  로그인 이후 유저 인증이 필요할때 호출 -> 토큰을 헤더에 자동으로 추가
    public static Retrofit RetrofitConnection(final String authToken) {
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new HttpLoggingInterceptor()
                        .setLevel(HttpLoggingInterceptor.Level.BASIC)
                        .setLevel(HttpLoggingInterceptor.Level.BODY)
                        .setLevel(HttpLoggingInterceptor.Level.HEADERS))
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request newRequest = chain.request().newBuilder()
                                .addHeader("Authorization", authToken)
                                .build();
                        return chain.proceed(newRequest);
                    }
                }).build();

        Retrofit retrofit2 = new Retrofit.Builder()
                .client(client)
//                .baseUrl("http://43.200.22.195:8000/")
                .baseUrl("http://192.168.0.18:8000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit2;
    }


    public static ApiClient getInstance() {
        if (instance == null) {
            instance = new ApiClient();
        }
        return instance;
    }

    public static UserService getService() {
        UserService userService = getRetrofit().create(UserService.class);

        return userService;
    }

    public static UserService getService2(String authToken) {
        UserService userService2 = RetrofitConnection(authToken).create(UserService.class);

        return userService2;
    }

    public static UserService getService3(String authToken) {
        UserService userService3 = getRetrofit2(authToken).create(UserService.class);

        return userService3;
    }

    public static String getAuthToken() {

        return authToken;
    }

    public static void setAuthToken(String authToken) {

        ApiClient.authToken = authToken;
    }

}
