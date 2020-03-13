package com.codingnew.rentbuysell;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {
    public static final int MSG_left=0;
    public static final int MSG_right=1;


    private Context mContext;
    private List<getchats> mgetchats;
    private String imageUrl;

    public MessageAdapter(Context mContext, List<getchats> mgetchats, String imageUrl) {
        this.mContext = mContext;
        this.mgetchats = mgetchats;
        this.imageUrl = imageUrl;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if(viewType==MSG_right) {
            view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_right, parent, false);
            return new MessageAdapter.ViewHolder(view);
        }
         else
        {
            view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_left, parent, false);
            return new MessageAdapter.ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final getchats users= mgetchats.get(position);
        holder.showMessages.setText(users.getMessage());
        Glide.with(mContext).load(imageUrl).into(holder.profilepic);

    }

    @Override
    public int getItemCount() {
        return mgetchats.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView showMessages;
        public ImageView profilepic;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            showMessages=itemView.findViewById(R.id.showmessages);
            profilepic=itemView.findViewById(R.id.profile_image);

        }

    }

    @Override
    public int getItemViewType(int position) {
         FirebaseAuth mAuth = FirebaseAuth.getInstance();
        if(mgetchats.get(position).getSendeR().equals(mAuth.getUid().toString()))
            return MSG_right;
        else
            return MSG_left;
    }
}
