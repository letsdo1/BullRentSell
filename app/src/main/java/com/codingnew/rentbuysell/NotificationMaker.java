package com.codingnew.rentbuysell;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class NotificationMaker {


public static void showNotification(Context context,String title,String mess) {
   Intent intent = new Intent(context, Rent.class);
    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    PendingIntent pendingIntent = PendingIntent.getActivity(context, 100, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, Constants.CHANNEL_ID);
    notificationBuilder.setContentTitle(title);
    notificationBuilder.setAutoCancel(true);
    notificationBuilder.setPriority(Notification.PRIORITY_HIGH);
    //notificationBuilder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
    notificationBuilder.setContentIntent(pendingIntent);
    notificationBuilder.setStyle(new NotificationCompat.BigTextStyle().bigText(mess));
    notificationBuilder.setContentText(mess);
    notificationBuilder.setDefaults(Notification.DEFAULT_VIBRATE);
    notificationBuilder.setSmallIcon(R.drawable.logo);


    NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
    notificationManagerCompat.notify(1, notificationBuilder.build());

}




    private int getNotificationIcon(NotificationCompat.Builder notificationBuilder) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            int color = 0x036085;
            notificationBuilder.setColor(color);
            return R.mipmap.ic_launcher;

        } else {
            return R.mipmap.ic_launcher;
        }
    }

}
