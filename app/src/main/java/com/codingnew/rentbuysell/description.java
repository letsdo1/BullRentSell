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
import android.widget.Button;
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
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class description extends AppCompatActivity {
    LinearLayout callseller,messageseller;
    TextView reportAdmin;
    private static final int Request_Call=2;
    FirebaseAuth mAuth= FirebaseAuth.getInstance();
    private FirebaseFirestore db= FirebaseFirestore.getInstance();
    public  Map<String, String> TIMESTAMP;
    public String Reciverid,mode;
    ImageView back;
    Button see_seller;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description);
        Intent i=getIntent();
        Reciverid=i.getStringExtra("UID");
        mode=i.getStringExtra("Mode");
        if(mode!=null) {
            if (!((mode.equals("ON SALE") || mode.equals("ON RENT")))) {
               ImageView rupeeimage=findViewById(R.id.rupeeimage);
               rupeeimage.setVisibility(View.GONE);
                back=findViewById(R.id.back_btn);
                back.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                        Intent i=new Intent(description.this,ListGiveAwayItems.class);
                        startActivity(i);
                    }
                });
            }
        }
        see_seller=findViewById(R.id.see_seller);
        see_seller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=getIntent();
                String name = i.getStringExtra("Name");
                String desc = i.getStringExtra("Shortdesc");
                String price = i.getStringExtra("Price");
                String imageurl=i.getStringExtra("ImageURL");
                String Mobile_No=i.getStringExtra("Mobile_no");
                Intent i2=new Intent(description.this,user_profile.class);
                i2.putExtra("UID",Reciverid);
                i2.putExtra("Mobile_no",Mobile_No);
                i2.putExtra("Name",name);
                i2.putExtra("Shortdesc",desc);
                i2.putExtra("Price",price);
                i2.putExtra("ImageURL",imageurl);
                i2.putExtra("Mode",mode);
                startActivity(i2);
                finish();

            }
        });
        back=findViewById(R.id.back_btn);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent i=new Intent(description.this,Drawer.class);
                startActivity(i);
            }
        });




        callseller=findViewById(R.id.call_seller);
        messageseller=findViewById(R.id.message_seller);
        reportAdmin=findViewById(R.id.ReportA);
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

                //Toast.makeText(description.this, "Send Message clicked", Toast.LENGTH_SHORT).show();
                sendmessage(Reciverid);

            }
        });
        getdata();


    }

    private void getdata()
    {
        if(getIntent().hasExtra("Name")&&getIntent().hasExtra("Price")&&getIntent().hasExtra("Shortdesc")&&getIntent().hasExtra("ImageURL")&&getIntent().hasExtra("Mobile_no"))
    {
        Intent i = getIntent();
        String name = i.getStringExtra("Name");
        String desc = i.getStringExtra("Shortdesc");
        String price = i.getStringExtra("Price");
        String imageurl=i.getStringExtra("ImageURL");
        String cat=i.getStringExtra("CATEGORY");
       // Toast.makeText(this,cat, Toast.LENGTH_SHORT).show();
        addData(name,desc,price,imageurl);
    }

    }
    private void addData(String name,String desc,String price,String imageurl){
        TextView Name,Desc,Price;
        ImageView pic;
        Name=findViewById(R.id.text_name);
        Desc=findViewById(R.id.text_desc);
        Price=findViewById(R.id.Price);
        pic=findViewById(R.id.image_pic);
        Name.setText(name);
        Desc.setText(desc);
        Price.setText(price);
        Glide.with(description.this).load(imageurl).into(pic);
        //Picasso.get().load(imageurl).fit().into(pic);
    }
    private void makephonecall(String mobile_No){
        if(mobile_No.trim().length()>0) {
            if(ContextCompat.checkSelfPermission(description.this, Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(description.this,new String[]{Manifest.permission.CALL_PHONE},Request_Call);
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
                                Toast.makeText(description.this, "Document snapshot null", Toast.LENGTH_SHORT).show();
                            }
                        }else{

                            Toast.makeText(description.this, "Task is unsuccessfull because"+task.getException(), Toast.LENGTH_SHORT).show();
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
                                    Toast.makeText(description.this, "Document snapshot null", Toast.LENGTH_SHORT).show();
                                }
                            }else{

                                Toast.makeText(description.this, "Task is unsuccessfull because"+task.getException(), Toast.LENGTH_SHORT).show();
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
                        Intent i=new Intent(description.this,Chat.class);
                        startActivity(i);

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(description.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();

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
                        Toast.makeText(description.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
    }
    public void status(String status)
    { Map<String, Object> data = new HashMap<>();
        data.put("status",status);
        db.collection("users").document(mAuth.getUid()).collection("Chats").document(Reciverid).set(data, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(description.this,"Successful",Toast.LENGTH_LONG).show();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(description.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });

    }


}

