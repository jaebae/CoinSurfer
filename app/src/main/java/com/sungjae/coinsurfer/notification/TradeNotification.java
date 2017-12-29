package com.sungjae.coinsurfer.notification;


import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.sungjae.coinsurfer.R;
import com.sungjae.coinsurfer.activity.MainActivity;

import static android.app.Notification.CATEGORY_MESSAGE;
import static android.support.v4.app.NotificationCompat.PRIORITY_MAX;

public class TradeNotification {
    private Context mContext;


    public TradeNotification(Context context) {
        mContext = context;
    }

    public Notification showNotification(String msg) {
        Intent Intent = new Intent(mContext, MainActivity.class);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(mContext, 0, Intent, PendingIntent.FLAG_UPDATE_CURRENT);

        return new NotificationCompat.Builder(mContext)
                .setSmallIcon(R.drawable.coin_icon)
                .setContentTitle(msg)
                .setAutoCancel(true)
                .setWhen(System.currentTimeMillis())
                .setDefaults(0)
                .setCategory(CATEGORY_MESSAGE)
                .setPriority(PRIORITY_MAX)
                .setVisibility(android.app.Notification.VISIBILITY_PUBLIC)
                .setContentIntent(resultPendingIntent)
                .build();
    }
}
