package com.example.saraelsheemi.pinmate.controllers;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.saraelsheemi.pinmate.controllers.AsynchTasks.AsynchTaskPut;
import com.example.saraelsheemi.pinmate.models.User;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;


public class NotificationsMessagingService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d("myfirebaseinstance", "Refreshed token: " + refreshedToken);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
       sendRegistrationToServer(refreshedToken);
    }

    private void sendRegistrationToServer(String refreshedToken) {
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.apply();
        Gson gson = new Gson();
        String json = sharedPreferences.getString("user_info", "");
        User user = gson.fromJson(json, User.class);

        String token = "{\"notification_token\":\""+refreshedToken+"\"}";

        AsynchTaskPut asynchTaskPut = new AsynchTaskPut(token, getApplicationContext(), new EventListener<String>() {
            @Override
            public void onSuccess(String object) {
                JSONObject jsonObject;
                Boolean ok = false;
                try {
                    jsonObject = new JSONObject(object);
                    ok = jsonObject.getBoolean("ok");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (ok) {
                    Log.e("Update", "notification updated successfully");
                } else {
                    Log.e("Update failed", "notification not updated");
                }
            }

            @Override
            public void onFailure(Exception e) {
                Log.e("Update failed", "server error");
            }
        });
        asynchTaskPut.execute(Constants.UPDATE_USER + user.getId());
    }
}