package com.sharpflux.deliveryboy2;

import android.app.Notification;
import android.app.NotificationManager;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.widget.RemoteViews;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;


public class MyMessagingServices extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        showNotification(remoteMessage.getNotification().getTitle(),remoteMessage.getNotification().getBody());
    }

    public void  showNotification(String title , String message){

        RemoteViews contentView = new RemoteViews(getPackageName(), R.layout.notification_one);
        contentView.setImageViewResource(R.id.image, R.mipmap.ic_launcher);
        contentView.setTextViewText(R.id.title, "Custom notification");
        contentView.setTextViewText(R.id.text, "This is a custom layout");

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this,"MyNotification")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContent(contentView);
        mBuilder.setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 });
        mBuilder.setSound(Settings.System.DEFAULT_RINGTONE_URI);
        Notification notification = mBuilder.build();
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notification.defaults |= Notification.DEFAULT_SOUND;
        notification.defaults |= Notification.DEFAULT_VIBRATE;


        NotificationManagerCompat manager=NotificationManagerCompat.from(this);
                manager.notify(999,mBuilder.build());


    }
}
