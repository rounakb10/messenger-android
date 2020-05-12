package com.dark.messenger2;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.dark.messenger2.Model.Chat;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder>{
    private List<Chat> mChat;
    private Context mContext;
    private String imageurl;

    private static final int MSG_TYPE_LEFT = 0;
    private static final int MSG_TYPE_RIGHT = 1;

    private FirebaseUser firebaseUser;

    public MessageAdapter(Context context , List<Chat> mChat, String imageurl) {
        this.mChat = mChat;
        mContext = context;
        this.imageurl = imageurl;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public CircleImageView civmProfile;
        public TextView show_message;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            civmProfile = itemView.findViewById(R.id.civmProfile);
            show_message = itemView.findViewById(R.id.show_message);

        }
    }

    @Override
    public int getItemViewType(int position) {

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if(mChat.get(position).getSender().equals(firebaseUser.getUid())){
            return MSG_TYPE_RIGHT;
        }
        else {
            return MSG_TYPE_LEFT;
        }

    }

    @NonNull
    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == MSG_TYPE_RIGHT) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item_right, parent, false);
            return new ViewHolder(v);
        }
        else{
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item_left, parent, false);
            return new ViewHolder(v);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.ViewHolder holder, int position) {
        Chat chat = mChat.get(position);
        holder.show_message.setText(chat.getMessage());

/*
        if(imageurl.equals("default")){
            holder.civmProfile.setImageResource(R.drawable.ic_launcher_foreground);
        }
        else{
            Glide.with(mContext).load(imageurl).into(holder.civmProfile);
        }
*/

    }


    @Override
    public int getItemCount() {
        return mChat.size();
    }
}


