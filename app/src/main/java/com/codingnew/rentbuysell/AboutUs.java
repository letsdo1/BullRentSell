package com.codingnew.rentbuysell;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AboutUs extends AppCompatActivity {
    ImageView pankaj,prince,back,codingnew_club_logo;
    LinearLayout l1,l2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us); back=findViewById(R.id.back_btn);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent i=new Intent(AboutUs.this,Drawer.class);
                startActivity(i);
            }
        });

        pankaj=findViewById(R.id.mempic1);
        prince=findViewById(R.id.mempic2);
        l1=findViewById(R.id.mem1);
        l2=findViewById(R.id.mem2);
        codingnew_club_logo=findViewById(R.id.codingnew_club_pic);
        String p1="https://firebasestorage.googleapis.com/v0/b/rentbuysell-4d576.appspot.com/o/pankaj.jpg?alt=media&token=ec622a25-02c5-484d-b737-96d7c62a4c36";
        String p2="https://firebasestorage.googleapis.com/v0/b/rentbuysell-4d576.appspot.com/o/prince.jpg?alt=media&token=b0ee8289-df58-42ee-b168-9d367b44c499";
        Glide.with(AboutUs.this).load(p1).into(pankaj);
        Glide.with(AboutUs.this).load(p2).into(prince);
        l1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent i = new Intent("android.intent.action.VIEW", Uri.parse("https://www.linkedin.com/in/pankaj-kumar-a512b8166/"));
                startActivity(i);



            }
        });
        l2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent i = new Intent("android.intent.action.VIEW", Uri.parse("https://www.linkedin.com/in/prince-prag-374329175/"));
                startActivity(i);


            }
        });
        codingnew_club_logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent i = new Intent("android.intent.action.VIEW", Uri.parse("https://www.facebook.com/codingnewclubiitg/"));
                startActivity(i);



            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        Intent i=new Intent(AboutUs.this,Drawer.class);
        startActivity(i);
    }
}


