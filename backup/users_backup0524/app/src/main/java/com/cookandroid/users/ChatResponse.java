package com.cookandroid.users;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ChatResponse implements Serializable {
    @SerializedName("message")
    public String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String fuck) {
        this.message = fuck;
    }
}
