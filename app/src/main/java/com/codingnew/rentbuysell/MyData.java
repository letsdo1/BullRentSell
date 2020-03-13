package com.codingnew.rentbuysell;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class MyData extends AppCompatActivity {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private user_product_adapter adapter;
    ImageView myinfopic,back;
    TextView myinfoname,myinfohostel,myinforollno;
    private description info;
    String upuid;
    public static users myuser = new users();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_data);
        myinfopic=findViewById(R.id.myinfopic);
        myinfoname=findViewById(R.id.myinfoname);
        myinfohostel=findViewById(R.id.myinfohostel);
        myinforollno=findViewById(R.id.myinforollno);
        put_userdata_header();
        setUpRecyclerview();
        back=findViewById(R.id.back_btn);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent i=new Intent(MyData.this,Drawer.class);
                startActivity(i);
            }
        });

    }

    @Override
    public void onBackPressed() {
        Intent i=new Intent(MyData.this,Drawer.class);
        startActivity(i);
    }

    /*private void getUserDetails(){
            String uid= mAuth.getCurrentUser().getUid();
            DocumentReference docref= db.collection("users").document(uid);
            docref.get().addOnCompleteListener(
                    new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot documentSnapshot = task.getResult();

                                if (documentSnapshot != null) {

                                    myuser.setHOSTEL(documentSnapshot.getString("Hostel"));
                                    myuser.setNAME(documentSnapshot.getString("Name"));
                                    myuser.setEMAIL(documentSnapshot.getString("Email"));
                                    myuser.setMOILE_NUMBER(documentSnapshot.getString("Mobile Number"));
                                    myuser.setROLL_NUMBER(documentSnapshot.getString("Roll Number"));
                                    myuser.setIMAGE_URL(documentSnapshot.getString("Image Url"));
                                    put_userdata_header();
                                    // Toast.makeText(Drawer.this,documentSnapshot.getString("Hostel")+documentSnapshot.getString("Name")+documentSnapshot.getString("Email"), Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(MyData.this, "Document snapshot null", Toast.LENGTH_SHORT).show();
                                }
                            }else{

                                Toast.makeText(MyData.this, "Task is unsuccessfull because"+task.getException(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

        }*/
    private void put_userdata_header() {
//        myinfoname.setText(myuser.getNAME());
//        myinfohostel.setText(myuser.getHOSTEL());
//        Toast.makeText(this, myuser.getNAME(), Toast.LENGTH_SHORT).show();
//        myinforollno.setText(myuser.getROLL_NUMBER());
//        String url_string = myuser.getIMAGE_URL();
//        Glide.with(this).load(url_string).into(myinfopic);
        final String uid= mAuth.getCurrentUser().getUid();
        DocumentReference docref= db.collection("users").document(uid);
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
                                Glide.with(MyData.this).load(url_string).into(myinfopic);
                                myinforollno.setText(documentSnapshot.getString("Roll Number"));
                                // Toast.makeText(Drawer.this,documentSnapshot.getString("Hostel")+documentSnapshot.getString("Name")+documentSnapshot.getString("Email"), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(MyData.this, "Document snapshot null", Toast.LENGTH_SHORT).show();
                            }
                        }else{

                            Toast.makeText(MyData.this, "Task is unsuccessfull because"+task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }



    private CollectionReference productref;
    private DocumentReference docref;
     private void setUpRecyclerview() {

         String useruid = mAuth.getCurrentUser().getUid();


         productref = db.collection("users").document(useruid).collection("Product");
         //Toast.makeText(this, productref.getId()+"This id", Toast.LENGTH_SHORT).show();

            Query query=productref;


         /*docref = db.collection("Sports and Accesories").document();
         docref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
             @Override
             public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                 if (task.isSuccessful()) {
                     DocumentSnapshot document = task.getResult();
                     upuid=document.toString();
                     if (document.exists()) {
                         Toast.makeText(MyData.this, "Such document Exists", Toast.LENGTH_SHORT).show();

                     } else {
                         Toast.makeText(MyData.this, "No such document ", Toast.LENGTH_SHORT).show();
                     }
                 } else {
                     Toast.makeText(MyData.this, "get failed with ", Toast.LENGTH_SHORT).show();
                 }
             }
         });*/


         RecyclerView recyclerView=findViewById(R.id.Myinfo);
         FirestoreRecyclerOptions<product_part> options;
         options=new FirestoreRecyclerOptions.Builder<product_part>().setQuery(query, product_part.class).build();
         adapter=new user_product_adapter(options,this);
         recyclerView.setHasFixedSize(true);
         recyclerView.setLayoutManager(new LinearLayoutManager(this));
         recyclerView.setAdapter(adapter);


//         productref = db.collection("Sports and Accesories");
//         options=new FirestoreRecyclerOptions.Builder<product_part>().setQuery(query,product_part.class).build();
//         adapter=new productAdapter(options,this);
//         recyclerView.setHasFixedSize(true);
//         recyclerView.setLayoutManager(new LinearLayoutManager(this));
//         recyclerView.setAdapter(adapter);

     }


    @Override
    protected void onStart() {
        super.onStart();
        if(adapter!=null)
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}
