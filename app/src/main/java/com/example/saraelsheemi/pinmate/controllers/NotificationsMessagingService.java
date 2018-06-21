package com.example.saraelsheemi.pinmate.controllers;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


public class NotificationsMessagingService extends FirebaseInstanceIdService {
    private final String SEND_TOKEN_URL = "";
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d("myfirebaseinstance", "Refreshed token: " + refreshedToken);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
      // sendRegistrationToServer(refreshedToken);
    }

    private void sendRegistrationToServer(String refreshedToken) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("refreshedToken",refreshedToken);

       // AsynchTaskPost asynchTaskPost = new AsynchTaskPost(refreshedToken);
       // asynchTaskPost.execute(SEND_TOKEN_URL);
    }
}