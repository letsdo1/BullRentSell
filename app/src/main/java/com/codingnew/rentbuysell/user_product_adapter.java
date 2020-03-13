package com.codingnew.rentbuysell;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.squareup.picasso.Picasso;

import java.util.List;

public class user_product_adapter extends FirestoreRecyclerAdapter<product_part,user_product_adapter.productHolder> {
    private Context mContext;


    public user_product_adapter(@NonNull FirestoreRecyclerOptions<product_part> options, Context mContext) {
        super(options);
        this.mContext = mContext;

    }

    //  public productAdapter(@NonNull FirestoreRecyclerOptions<Product_list> options) {
    //      super(options);
    //  }

    @Override
    protected void onBindViewHolder(@NonNull final productHolder productHolder, int i, @NonNull final product_part product_list) {
        productHolder.Name.setText(product_list.getName());
        productHolder.Shortdesc.setText(product_list.getDescription());
        //String s=String.valueOf(product_list.getPrice());
        productHolder.pPrice.setText(product_list.getPrice());
        String imURL=product_list.getImageUrl();
        Picasso.get().load(imURL).fit().into(productHolder.imageView);
        productHolder.Mode.setText(product_list.getMode());
        productHolder.listproduct.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {

               // Toast.makeText(mContext,product_list.getCategory(),Toast.LENGTH_SHORT).show();
                Intent i=new Intent(mContext,User_Description.class);
                i.putExtra("Name",product_list.getName());
                i.putExtra("Shortdesc",product_list.getDescription());
                i.putExtra("ImageURL",product_list.getImageUrl());
                i.putExtra("Price",product_list.getPrice());
                i.putExtra("Mobile_no",product_list.getMobileNo());
                i.putExtra("UID",product_list.getUid());
                i.putExtra("CATEGORY",product_list.getCategory());
                i.putExtra("PARENTID",product_list.getParentid());
                i.putExtra("UID",product_list.getUid());
                i.putExtra("public_feed",product_list.getPublic_feed());
                i.putExtra("mode",product_list.getMode());
               // Toast.makeText(mContext,product_list.getUid(), Toast.LENGTH_SHORT).show();
                mContext.startActivity(i);
            }
        });



    }

    @NonNull
    @Override
    public productHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.product_part,parent,false);
        return new productHolder(v);
    }

    public  class productHolder extends RecyclerView.ViewHolder {
        TextView Shortdesc;
        TextView Name;
        ImageView imageView;
        TextView pPrice,Mode;
        RelativeLayout listproduct;

        public productHolder(@NonNull View itemView) {
            super(itemView);
            Shortdesc=itemView.findViewById(R.id.textViewShortDesc);
            Name=itemView.findViewById(R.id.textViewTitle);
            pPrice=itemView.findViewById(R.id.textViewPrice);
            imageView=itemView.findViewById(R.id.imageView);
            listproduct=itemView.findViewById(R.id.list_product);
            Mode=itemView.findViewById(R.id.mode);

        }
    }
}
