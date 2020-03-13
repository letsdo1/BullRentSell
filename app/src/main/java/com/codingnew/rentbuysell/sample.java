package com.codingnew.rentbuysell;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import android.os.Bundle;
import android.util.Log;

public class sample extends AppCompatActivity {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();


    private productAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample);
        setUpRecyclerview();
    }


    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }


    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }


    private void setUpRecyclerview() {
        CollectionReference productref = db.collection("Sports");
        //Log.d("anna",productref.getPath().toString());
        Query query= productref;
        FirestoreRecyclerOptions<product_part> options = new FirestoreRecyclerOptions.Builder<product_part>().setQuery(query, product_part.class).build();
        Log.d("aaa",options.getSnapshots().toString());
        adapter = new productAdapter(options, this);
        RecyclerView recyclerView = findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }
}