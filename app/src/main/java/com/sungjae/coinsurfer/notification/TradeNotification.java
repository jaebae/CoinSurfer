package com.sungjae.coinsurfer.notification;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.sungjae.coinsurfer.R;
import com.sungjae.coinsurfer.activity.MainActivity;

import static android.app.Notification.CATEGORY_MESSAGE;

public class TradeNotification {
    private Context mContext;


    public TradeNotification(Context context) {
        mContext = context;
    }

    public Notification showNotification(String msg) {
        NotificationChannel nc = new NotificationChannel("CoinSurfer", "CoinSurfer", NotificationManager.IMPORTANCE_MIN);
        NotificationManager nm = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        nm.createNotificationChannel(nc);

        Intent Intent = new Intent(mContext, MainActivity.class);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(mContext, 0, Intent, PendingIntent.FLAG_UPDATE_CURRENT);

        return new Notification.Builder(mContext, nc.getId())
                .setSmallIcon(R.drawable.coin_icon)
                .setContentTitle(msg)
                .setAutoCancel(true)
                .setWhen(System.currentTimeMillis())
                .setCategory(CATEGORY_MESSAGE)
                .setVisibility(android.app.Notification.VISIBILITY_PUBLIC)
                .setContentIntent(resultPendingIntent).setSound(null, null)
                .build();
    }
}
