package com.example.demo1;

import android.content.Context;
import android.content.SharedPreferences;

public class UserCurrent {
    private String username, pass, loginid, userno;
    Context context;
    SharedPreferences sharedPreferences;

    public UserCurrent(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences("login_details",Context.MODE_PRIVATE);
    }

    public String getLoginid() {
        loginid = sharedPreferences.getString("loginid","");
        return loginid;
    }

    public void setLoginid(String loginid) {
        this.loginid = loginid;
        sharedPreferences.edit().putString("loginid",loginid).commit();
    }

    public String getUsername() {
        username = sharedPreferences.getString("username","");
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
        sharedPreferences.edit().putString("username", username).commit();
    }

    public String getPass() {
        pass = sharedPreferences.getString("pass","");
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
        sharedPreferences.edit().putString("pass",pass).commit();
    }

    public String getUserno() {
        userno = sharedPreferences.getString("userno","");
        return userno;
    }

    public void setUserno(String userno) {
        this.userno = userno;
        sharedPreferences.edit().putString("userno",userno).commit();
    }

    public void removeUser(){
        sharedPreferences.edit().clear().commit();

    }
}
