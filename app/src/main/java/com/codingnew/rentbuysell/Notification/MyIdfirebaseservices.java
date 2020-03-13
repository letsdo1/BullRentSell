package com.codingnew.rentbuysell.Notification;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.codingnew.rentbuysell.messageInterface;
import com.codingnew.rentbuysell.signIn;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyIdfirebaseservices extends FirebaseMessagingService {
    FirebaseAuth mAuth;
    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                String refreshToken = instanceIdResult.getToken();

                if (refreshToken != null){
                    updateToken(refreshToken);

                }
            }
        });
    }

    private void updateToken(String refreshToken) {
        if (FirebaseAuth.getInstance().getCurrentUser() != null){
            FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Token");//.child(firebaseUser.getUid()).child("Token");
            Token tokens = new Token(refreshToken);
            reference.child(firebaseUser.getUid()).setValue(tokens);
        }
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        SharedPreferences sp=getSharedPreferences("SP_USER",MODE_PRIVATE);
        String savedCurrent=sp.getString("Current_USERID","None");
        String sented= remoteMessage.getData().get("sented");
        //Toast.makeText(this,sented, Toast.LENGTH_SHORT).show();
        FirebaseUser fuser=FirebaseAuth.getInstance().getCurrentUser();
        if(fuser!=null && sented.equals(FirebaseAuth.getInstance().getUid())){
         if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            sendOreoNotification(remoteMessage);
         }
         else{
             sendNotification(remoteMessage);
         }

        }

    }
    private void sendOreoNotification(RemoteMessage remoteMessage){
        String user=remoteMessage.getData().get("user");
        String icon=remoteMessage.getData().get("icon");
        String title=remoteMessage.getData().get("title");
        String body=remoteMessage.getData().get("body");
        RemoteMessage.Notification notification=remoteMessage.getNotification();
        int j= Integer.parseInt(user.replaceAll("[\\D]",""));
        Intent i= new Intent(this, messageInterface.class);
        Bundle bundle=new Bundle();
        bundle.putString("UserId",user);
        i.putExtras(bundle);
        i.addFlags(i.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent=PendingIntent.getActivity(this,j,i,PendingIntent.FLAG_ONE_SHOT);

        Uri defaultsound= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        oreonotification oreonotification=new oreonotification(this);
        Notification.Builder builder=oreonotification.getOreoNotification(title,body,pendingIntent,defaultsound,icon);
        int p=0;
        if(j>0){
            p=j;
        }
        oreonotification.getManager().notify(p,builder.build());
    }
    private void sendNotification(RemoteMessage remoteMessage)
    {   String user=remoteMessage.getData().get("user");
        String icon=remoteMessage.getData().get("icon");
        String title=remoteMessage.getData().get("title");
        String body=remoteMessage.getData().get("body");
        RemoteMessage.Notification notification=remoteMessage.getNotification();
        int j= Integer.parseInt(user.replaceAll("[\\D]",""));
        Intent i= new Intent(this, messageInterface.class);
        Bundle bundle=new Bundle();
        bundle.putString("UserId",user);
        i.putExtras(bundle);
        i.addFlags(i.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent=PendingIntent.getActivity(this,j,i,PendingIntent.FLAG_ONE_SHOT);

        Uri defaultsound= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder=new NotificationCompat.Builder(this)
                .setSmallIcon(Integer.parseInt(icon))
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setSound(defaultsound)
                .setContentIntent(pendingIntent);
        NotificationManager noti=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
         int p=0;
         if(j>0){
             p=j;
         }
         noti.notify(p,builder.build());


    }
}
