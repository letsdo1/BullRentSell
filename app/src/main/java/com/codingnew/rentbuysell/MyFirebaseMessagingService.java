package com.codingnew.rentbuysell;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.HashMap;
import java.util.Map;


public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "FCM Service";
    private Bitmap bitmap;
    private Context context = this;
    public static final String FIREBASE_TOKEN = "NOT_ID";
    NotificationChannel mChannel;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
  FirebaseFirestore db= FirebaseFirestore.getInstance();
    private   String token;

    @Override
    public void onNewToken(@NonNull String refreshedToken) {
        super.onNewToken(refreshedToken);
        users.getInstance(this).put(FIREBASE_TOKEN, refreshedToken);
        if (refreshedToken!=null){
            token=refreshedToken;
            savetoken();
        }
        Log.d("Tok",refreshedToken.toString());

    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        if (remoteMessage.getNotification() != null) {
            //sendUserNotification( , );
            int importance = 0;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                importance = NotificationManager.IMPORTANCE_HIGH;
            }


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                mChannel = new NotificationChannel(Constants.CHANNEL_ID, "Noti", importance);
                NotificationManager notificationManager=getSystemService(NotificationManager.class);
                notificationManager.createNotificationChannel(mChannel);
            }

            NotificationMaker.showNotification(context,remoteMessage.getNotification().getTitle(),remoteMessage.getNotification().getBody());
        }

    }


    private void savetoken()


    {
        Map<String,Object> data = new HashMap<>();
        data.put(Constants.KEY_TOKEN,token);

        db.collection("users").document(mAuth.getCurrentUser().getUid()).collection("NOT_ID").document(mAuth.getCurrentUser().getUid()).set(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(context, token.toString(), Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });



    }






}
