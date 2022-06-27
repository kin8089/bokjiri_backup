package com.cookandroid.users;

import com.google.gson.annotations.SerializedName;

public class UserInfoResponse {
    @SerializedName("message")
    String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
