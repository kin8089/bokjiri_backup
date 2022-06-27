package com.cookandroid.users;

import java.io.Serializable;

public class PasswordResponse implements Serializable {

    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
