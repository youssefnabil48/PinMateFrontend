package com.example.saraelsheemi.pinmate.controllers;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.saraelsheemi.pinmate.models.User;
import com.google.gson.Gson;

public class SharedPreferencesUser {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Gson gson;
    Activity activity;


    public SharedPreferencesUser(Activity activity) {
        this.activity=activity;
        sharedPreferences = activity.getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.apply();
    }

    public User getUserData(){
        String json = sharedPreferences.getString("user_info","");
        Log.e("userINFO",json);
        gson = new Gson();
        return gson.fromJson(json,User.class);
    }
}
