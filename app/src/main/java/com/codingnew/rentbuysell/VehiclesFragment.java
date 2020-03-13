package com.codingnew.rentbuysell;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class VehiclesFragment extends Fragment {
    View xyz;
    FirestoreRecyclerOptions<product_part> prod;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FirebaseFirestore db= FirebaseFirestore.getInstance();
        View v= inflater.inflate(R.layout.activity_vehicles, container, false);

        CollectionReference productref= db.collection("Vehicles and Automobiles");
        Query query=productref.orderBy("myidint", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<product_part> options=new FirestoreRecyclerOptions.Builder<product_part>().setQuery(query,product_part.class).build();
        productAdapter adapter=new productAdapter(options,getContext());
        RecyclerView recyclerView= v.findViewById(R.id.recycler_vehicles);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
        adapter.startListening();

        return v;


    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
}
