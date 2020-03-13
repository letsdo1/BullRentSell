package com.codingnew.rentbuysell;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class user_profile extends AppCompatActivity {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private user_product_adapter adapter;
    private static final int Request_Call=2;
    ImageView myinfopic,back;
    LinearLayout callseller,messageseller;
    TextView myinfoname,myinfohostel,myinforollno;
    private description info;
    String Reciverid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        myinfopic=findViewById(R.id.myinfopic);
        myinfoname=findViewById(R.id.myinfoname);
        myinfohostel=findViewById(R.id.myinfohostel);
        myinforollno=findViewById(R.id.myinforollno);
        Intent i=getIntent();
        Reciverid=i.getStringExtra("UID");
        final String name = i.getStringExtra("Name");
        final String desc = i.getStringExtra("Shortdesc");
        final String price = i.getStringExtra("Price");
        final String imageurl=i.getStringExtra("ImageURL");
        final String Mobile_No=i.getStringExtra("Mobile_no");
        final String mode=i.getStringExtra("Mode");

        back=findViewById(R.id.back_btn);
        put_userdata_header();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent i=new Intent(user_profile.this,description.class);
                i.putExtra("UID",Reciverid);
                i.putExtra("Mobile_no",Mobile_No);
                i.putExtra("Name",name);
                i.putExtra("Shortdesc",desc);
                i.putExtra("Price",price);
                i.putExtra("ImageURL",imageurl);
                i.putExtra("Mode",mode);
                startActivity(i);
            }
        });
        callseller=findViewById(R.id.call_seller);
        messageseller=findViewById(R.id.message_seller);
        callseller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Mobile_No=getIntent().getStringExtra("Mobile_no");
                makephonecall(Mobile_No);
            }
        });
        messageseller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendmessage(Reciverid);

            }
        });
    }

    private void put_userdata_header() {
        DocumentReference docref= db.collection("users").document(Reciverid);
        docref.get().addOnCompleteListener(
                new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot documentSnapshot = task.getResult();

                            if (documentSnapshot != null) {

                                String names,email,pic_url;
                                myinfoname.setText(documentSnapshot.getString("Name"));
                                myinfohostel.setText(documentSnapshot.getString("Hostel"));
                                String url_string = documentSnapshot.getString("Image Url");
                                Glide.with(user_profile.this).load(url_string).into(myinfopic);
                                myinforollno.setText(documentSnapshot.getString("Roll Number"));
                            } else {
                                Toast.makeText(user_profile.this, "Document snapshot null", Toast.LENGTH_SHORT).show();
                            }
                        }else{

                            Toast.makeText(user_profile.this, "Task is unsuccessful because"+task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    private void makephonecall(String mobile_No){
        if(mobile_No.trim().length()>0) {
            if(ContextCompat.checkSelfPermission(user_profile.this, Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(user_profile.this,new String[]{Manifest.permission.CALL_PHONE},Request_Call);
            }
            else
            {
                String dial="tel:"+mobile_No;
                startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
            }


        }
        else
            Toast.makeText(this,"Mobile number is not valid",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==Request_Call)
        {    String Mobile_No=getIntent().getStringExtra("Mobile_no");

            if(grantResults.length >0 &&  grantResults[0]==PackageManager.PERMISSION_GRANTED)
                makephonecall(Mobile_No);
            else
                Toast.makeText(this,"Permission Denied",Toast.LENGTH_SHORT).show();
        }

    }
    private void sendmessage(final String UserID)
    {
        if(mAuth.getUid().equals(UserID))
            Toast.makeText(this, "You can't send message to your self", Toast.LENGTH_SHORT).show();
        else
        {   //Toast.makeText(description.this, UserID, Toast.LENGTH_SHORT).show();
            DocumentReference docref= db.collection("users").document(UserID);
            docref.get().addOnCompleteListener(
                    new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot documentSnapshot = task.getResult();

                                if (documentSnapshot != null) {
                                    String username,imageUrl;
                                    username=documentSnapshot.getString("Name");
                                    imageUrl = documentSnapshot.getString("Image Url");
                                    putchatsenderdata(username,imageUrl,UserID);

                                } else {
                                    Toast.makeText(user_profile.this, "Document snapshot null", Toast.LENGTH_SHORT).show();
                                }
                            }else{

                                Toast.makeText(user_profile.this, "Task is unsuccessfull because"+task.getException(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
            DocumentReference docref1= db.collection("users").document(mAuth.getUid());
            docref1.get().addOnCompleteListener(
                    new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot documentSnapshot = task.getResult();

                                if (documentSnapshot != null) {
                                    String username,imageUrl;
                                    username=documentSnapshot.getString("Name");
                                    imageUrl = documentSnapshot.getString("Image Url");
                                    putchatreceiverdata(username,imageUrl,UserID);

                                } else {
                                    Toast.makeText(user_profile.this, "Document snapshot null", Toast.LENGTH_SHORT).show();
                                }
                            }else{

                                Toast.makeText(user_profile.this, "Task is unsuccessfull because"+task.getException(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });




        }
    }
    public void putchatsenderdata(String username,String imageUrl,String receiverid)
    {
        // Toast.makeText(this, "putchatsenderstarted", Toast.LENGTH_SHORT).show();
        Map<String, Object> data = new HashMap<>();
        data.put("username",username);
        data.put("imageUrl",imageUrl);
        data.put("user_id",receiverid);
        String time=String.valueOf(System.currentTimeMillis());
        data.put("Servertime",time);
        //data.put("status","");
        db.collection("users").document(mAuth.getUid()).collection("Chats").document(receiverid).set(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //Toast.makeText(description.this,"Successful202",Toast.LENGTH_LONG).show();
                        finish();
                        Intent i=new Intent(user_profile.this,Chat.class);
                        startActivity(i);

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(user_profile.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
    }
    public void putchatreceiverdata(String username,String imageUrl,String receiverid)
    {   //Toast.makeText(this, "putchatreceiverstarted", Toast.LENGTH_SHORT).show();
        Map<String, Object> data = new HashMap<>();
        data.put("username",username);
        data.put("imageUrl",imageUrl);
        data.put("user_id",mAuth.getUid());
        data.put("status"," ");
        String time=String.valueOf(System.currentTimeMillis());
        data.put("Servertime",time);
        //data.put("status","online");
        db.collection("users").document(receiverid).collection("Chats").document(mAuth.getUid()).set(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //Toast.makeText(description.this,"Successful",Toast.LENGTH_LONG).show();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(user_profile.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
    }
}
