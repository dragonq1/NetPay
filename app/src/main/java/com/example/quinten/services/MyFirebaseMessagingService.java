package com.example.quinten.services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.example.quinten.netpay.MainActivity;
import com.example.quinten.netpay.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "FCM Service";
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // TODO: Handle FCM messages here.
        // If the application is in the foreground handle both data and notification messages here.
        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated.
        Log.d(TAG, "From: " + remoteMessage.getFrom());


        if(remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());

            if(remoteMessage.getData().size() > 100) {
                Log.d(TAG, "Data payload to big");
            }else{
                handleNow(remoteMessage);
            }
        }

        if(remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message notification body: " + remoteMessage.getNotification().getBody());
        }


    }

    public void handleNow(RemoteMessage message) {

        //Kleur goed zetten
        int colorAccent = R.color.colorAccent;

        //Intent en pending intent aanmaken
        Intent notifyIntent = new Intent(this, MainActivity.class);
        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent notifyPendingIntent = PendingIntent.getActivity(
                this, 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT
        );

        //Notificatie aannmaken
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setContentText(message.getData().get("message"));
        mBuilder.setContentTitle(message.getData().get("title"));
        mBuilder.setColor(getColor(colorAccent));
        mBuilder.setSmallIcon(R.drawable.ic_not_icon);
        mBuilder.setPriority(NotificationCompat.PRIORITY_HIGH);
        mBuilder.setContentIntent(notifyPendingIntent);
        mBuilder.setAutoCancel(true);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, mBuilder.build());
    }
}
