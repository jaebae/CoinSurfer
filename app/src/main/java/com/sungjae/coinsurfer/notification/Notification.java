package com.sungjae.coinsurfer.notification;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;

import com.samsung.android.sdk.SsdkUnsupportedException;
import com.samsung.android.sdk.richnotification.Srn;
import com.samsung.android.sdk.richnotification.SrnImageAsset;
import com.samsung.android.sdk.richnotification.SrnRichNotification;
import com.samsung.android.sdk.richnotification.SrnRichNotificationManager;
import com.sungjae.coinsurfer.R;

public class Notification {
    private Context mContext;

    private SrnRichNotificationManager mRichNotificationManager;

    public Notification(Context context) {
        mContext = context;
        Srn srn = new Srn();
        try {
            srn.initialize(mContext);
        } catch (SsdkUnsupportedException e) {
        }

        mRichNotificationManager = new SrnRichNotificationManager(mContext);
        mRichNotificationManager.start();

        SrnRichNotification myRichNotification = new SrnRichNotification(mContext);
        myRichNotification.setIcon(getAppIcon());
        myRichNotification.setTitle("Coin surfer");


    }

    @NonNull
    private SrnImageAsset getAppIcon() {
        Bitmap myAppIconBitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.coin_wave_icon);
        return new SrnImageAsset(mContext, "app_icon", myAppIconBitmap);
    }

    /*public void showNotification() {
        try {
            NotificationManager mNotificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationCompat.Builder mBuilder;
            Intent Intent = new Intent(mContext, MainActivity.class);
            PendingIntent resultPendingIntent = PendingIntent.getActivity(
                    mContext,
                    0,
                    Intent,
                    PendingIntent.FLAG_UPDATE_CURRENT
            );
            if (!TextUtils.isEmpty(DataMap.readString(DataMap.NOTIFICATION_CONTENT))) {
                mBuilder = createNotification("CUT OFF", DataMap.readString(DataMap.NOTIFICATION_CONTENT), android.app.Notification.DEFAULT_ALL);
                mNotificationManager.notify(2, mBuilder.build());
                DataMap.writeString(DataMap.NOTIFICATION_CONTENT, "");
            }

            if (!TextUtils.isEmpty(DataMap.readString(DataMap.ERROR_TOAST_CONTENT))) {
                mBuilder = createNotification("ERROR", DataMap.readString(DataMap.ERROR_TOAST_CONTENT), 0);
                //startForeground(1, mBuilder.build());
                Toast.makeText(this, DataMap.readString(DataMap.ERROR_TOAST_CONTENT), Toast.LENGTH_LONG).show();
                DataMap.writeString(DataMap.ERROR_TOAST_CONTENT, "");
            } else {
                mBuilder = createNotification("등락", createUpDownStatus(), 0);
                mBuilder.setContentIntent(resultPendingIntent);
            }

            startForeground(1, mBuilder.build());

        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

    }

    private NotificationCompat.Builder createNotification(String title, String content, int alert) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext)
                .setSmallIcon(R.drawable.coin_wave_icon)
                .setContentTitle(title)
                .setContentTitle(content)
                .setAutoCancel(true)
                .setWhen(System.currentTimeMillis())
                .setDefaults(alert)
                .setCategory(android.app.Notification.CATEGORY_MESSAGE)
                .setPriority(IMPORTANCE_HIGH)
                .setVisibility(android.app.Notification.VISIBILITY_PUBLIC);

        return builder;
    }*/
}
