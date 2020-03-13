package com.codingnew.rentbuysell;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class adapter extends RecyclerView.Adapter<adapter.adaptervievhoder>{
    public ArrayList<product_part> feed;
    public Context mContext;

    public adapter(ArrayList<product_part> feed, Context mContext) {
        this.feed = feed;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public adaptervievhoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.product_part,parent,false);
        return new adapter.adaptervievhoder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull adaptervievhoder productHolder, int position) {
        final product_part product_list=feed.get(position);
        productHolder.Name.setText(product_list.getName());
        productHolder.Shortdesc.setText(product_list.getDescription());
        productHolder.pPrice.setText(product_list.getPrice());
        String imURL=product_list.getImageUrl();
        Picasso.get().load(imURL).fit().into(productHolder.imageView);
        productHolder.Mode.setText(product_list.getMode());


        productHolder.listproduct.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                // Toast.makeText(mContext,product_list.getname(),Toast.LENGTH_SHORT).show();
                Intent i=new Intent(mContext,description.class);
                i.putExtra("Name",product_list.getName());
                i.putExtra("Shortdesc",product_list.getDescription());
                i.putExtra("ImageURL",product_list.getImageUrl());
                i.putExtra("Price",product_list.getPrice());
                i.putExtra("Mobile_no",product_list.getMobileNo());
                i.putExtra("CATEGORY",product_list.getCategory());
                i.putExtra("UID",product_list.getUid());
               // Toast.makeText(mContext,product_list.getUid(), Toast.LENGTH_SHORT).show();
                mContext.startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return feed.size();
    }

    public class adaptervievhoder extends RecyclerView.ViewHolder{
        TextView Shortdesc;
        TextView Name;
        ImageView imageView;
        TextView pPrice;
        TextView Mode;
        RelativeLayout listproduct;

        public adaptervievhoder(@NonNull View itemView) {
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
