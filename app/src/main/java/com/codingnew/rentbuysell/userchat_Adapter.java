package com.codingnew.rentbuysell;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class userchat_Adapter extends FirestoreRecyclerAdapter<chat_users,userchat_Adapter.ViewHolder> {
    private Context mContext;
    private boolean isonline;

    public userchat_Adapter(@NonNull FirestoreRecyclerOptions<chat_users> options, Context mContext,boolean isonline) {
        super(options);
        this.mContext = mContext;
        this.isonline = isonline;
    }


    @Override
    protected void onBindViewHolder(@NonNull ViewHolder viewHolder, int i, @NonNull final chat_users chat_users) {
        viewHolder.Username.setText(chat_users.getUsername());
        Glide.with(mContext).load(chat_users.getImageUrl()).into(viewHolder.profilepic);
//        if(isonline) {
//            if (chat_users.getStatus().equals("Online")) {
//                viewHolder.online.setVisibility(View.VISIBLE);
//                viewHolder.offline.setVisibility(View.GONE);
//            } else {
//                viewHolder.offline.setVisibility(View.VISIBLE);
//                viewHolder.online.setVisibility(View.GONE);
//            }
//        }
//        else { viewHolder.offline.setVisibility(View.GONE);
//               viewHolder.online.setVisibility(View.GONE);
//
//        }
        viewHolder.chatinterface.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(mContext,messageInterface.class);
                i.putExtra("UserId",chat_users.getUser_id());
                i.putExtra("UseName",chat_users.getUsername());
                i.putExtra("ImageUrl",chat_users.getImageUrl());
                mContext.startActivity(i);
           }
        });

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v =LayoutInflater.from(parent.getContext()).inflate(R.layout.user_chat_show,parent,false);
        return new userchat_Adapter.ViewHolder(v);
    }


//    @NonNull
//    @Override
//    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View v =LayoutInflater.from(parent.getContext()).inflate(R.layout.user_chat_show,parent,false);
//        return new userchat_Adapter.ViewHolder(v);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
//        final chat_users users= mUser.get(position);
//        holder.Username.setText(users.getUsername());
//        Glide.with(mContext).load(users.getImageUrl()).into(holder.profilepic);
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i=new Intent(mContext,messageInterface.class);
//                i.putExtra("UserId",users.getUser_id());
//                mContext.startActivity(i);
//            }
//        });
//    }
//
//    @Override
//    public int getItemCount() {
//        return mUser.size();
//    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView Username;
        public ImageView profilepic;
        public ImageView online;
        public ImageView offline;
        public RelativeLayout chatinterface;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            Username=itemView.findViewById(R.id.username);
            profilepic=itemView.findViewById(R.id.profile_image);
            online=itemView.findViewById(R.id.online);
            offline=itemView.findViewById(R.id.offline);
            chatinterface=itemView.findViewById(R.id.chatinterface);

        }
    }
}
