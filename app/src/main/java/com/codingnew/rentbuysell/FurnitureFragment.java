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

public class FurnitureFragment extends Fragment {
    View xyz;
    FirestoreRecyclerOptions<product_part> prod;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FirebaseFirestore db= FirebaseFirestore.getInstance();
        View v= inflater.inflate(R.layout.activity_furniture, container, false);

        CollectionReference productref= db.collection("Home and Furniture");
        Query query=productref.orderBy("myidint", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<product_part> options=new FirestoreRecyclerOptions.Builder<product_part>().setQuery(query,product_part.class).build();
        productAdapter adapter=new productAdapter(options,getContext());
        RecyclerView recyclerView= v.findViewById(R.id.recycler_furniture);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
        adapter.startListening();

        return v;


    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

       /*
        prod.add(new product_part("sale","https://firebasestorage.googleapis.com/v0/b/rentbuysell-4d576.appspot.com/o/images%2F8aa4016d-04bf-4d6d-8ae5-e80ff6b31a56?alt=media&token=5b52436b-3231-415e-beb8-71a2b9c01694","99999","electronics","viraj","123","ass"));
        prod.add(new product_part("rent","https://firebasestorage.googleapis.com/v0/b/rentbuysell-4d576.appspot.com/o/images%2F8aa4016d-04bf-4d6d-8ae5-e80ff6b31a56?alt=media&token=5b52436b-3231-415e-beb8-71a2b9c01694","22222","electronics","viraj","123","ass"));
        prod.add(new product_part("give","https://firebasestorage.googleapis.com/v0/b/rentbuysell-4d576.appspot.com/o/images%2F8aa4016d-04bf-4d6d-8ae5-e80ff6b31a56?alt=media&token=5b52436b-3231-415e-beb8-71a2b9c01694","23232","electronics","viraj","123","ass"));
        prod.add(new product_part("take","https://firebasestorage.googleapis.com/v0/b/rentbuysell-4d576.appspot.com/o/images%2F8aa4016d-04bf-4d6d-8ae5-e80ff6b31a56?alt=media&token=5b52436b-3231-415e-beb8-71a2b9c01694","95565","electronics","viraj","123","ass"));
        prod.add(new product_part("do","https://firebasestorage.googleapis.com/v0/b/rentbuysell-4d576.appspot.com/o/images%2F8aa4016d-04bf-4d6d-8ae5-e80ff6b31a56?alt=media&token=5b52436b-3231-415e-beb8-71a2b9c01694","00000","electronics","viraj","123","ass"));
*/


    }
}
