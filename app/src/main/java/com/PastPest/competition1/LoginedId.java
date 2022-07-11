package com.PastPest.competition1;

import android.app.Application;

public class LoginedId extends Application {
    private String loginedId;

    public String getId(){
        return loginedId;
    }
    public void setId(String loginedId) {
        this.loginedId = loginedId;
    }
}