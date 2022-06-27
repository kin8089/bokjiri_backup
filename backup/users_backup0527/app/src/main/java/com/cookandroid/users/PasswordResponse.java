package com.cookandroid.users;

import java.io.Serializable;

public class PasswordResponse implements Serializable {

    private String Accept;

    public String getDetail() {
        return Accept;
    }

    public void setDetail(String detail) {
        this.Accept = detail;
    }
}
