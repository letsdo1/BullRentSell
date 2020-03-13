package com.codingnew.rentbuysell;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.codingnew.rentbuysell.Notification.Token;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;

public class Chat extends AppCompatActivity {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser currentuser = mAuth.getCurrentUser();
    private userchat_Adapter cadapter;
    private ArrayList<chat_users> muser;
    ImageView back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        back=findViewById(R.id.back_btn);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent i=new Intent(Chat.this,Drawer.class);
                startActivity(i);
            }
        });
        muser= new ArrayList<>();
        setUpRecyclerview();
        updateToken(FirebaseInstanceId.getInstance().getToken());

    }
    Intent i=getIntent();
    private DocumentReference docref;
    private void setUpRecyclerview() {

        RecyclerView recyclerView=findViewById(R.id.user_chat_recyclerview);
        Query query=db.collection("users").document(mAuth.getUid()).collection("Chats").orderBy("Servertime", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<chat_users> options;
        options=new FirestoreRecyclerOptions.Builder<chat_users>().setQuery(query, chat_users.class).build();
        cadapter=new userchat_Adapter(options,this,true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(cadapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        cadapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        cadapter.stopListening();
    }
    private void updateToken(String token)
    {
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference("Token");
        Token token1=new Token(token);
        ref.child(mAuth.getUid()).setValue(token1);

    }

    @Override
    public void onBackPressed() {
        Intent i=new Intent(Chat.this,Drawer.class);
        startActivity(i);
    }

}

