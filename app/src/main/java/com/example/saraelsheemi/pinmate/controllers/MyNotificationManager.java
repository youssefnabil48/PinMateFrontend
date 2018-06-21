package com.example.saraelsheemi.pinmate.controllers;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.example.saraelsheemi.pinmate.R;
import com.example.saraelsheemi.pinmate.views.Home;

public class MyNotificationManager {

    private Context context;
    private static MyNotificationManager mInstance;

    public MyNotificationManager(Context context) {
        this.context = context;
    }

    public static synchronized MyNotificationManager getInstance(Context context){
        if(mInstance == null)
            mInstance = new MyNotificationManager(context);
        return mInstance;
    }

    public void displayNotification(String title, String body){

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context,Constants.CHANNEL_ID)
                .setSmallIcon(R.drawable.image)
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setOnlyAlertOnce(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        Intent intent = new Intent(context,Home.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentIntent(pendingIntent);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if(notificationManager != null) {
            notificationManager.notify(1,builder.build());
        }
    }

}
