package com.dark.messenger2;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.dark.messenger2.Model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RecursiveAction;

import de.hdodenhof.circleimageview.CircleImageView;

public class PersonAdapter extends RecyclerView.Adapter<PersonAdapter.ViewHolder>{
    private List<User> data;
    private Context mContext;

    public PersonAdapter(Context context , List<User> data) {
        this.data = data;
        mContext = context;

    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public CircleImageView civPic;
        public TextView tvName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            civPic = itemView.findViewById(R.id.civPic);
            tvName = itemView.findViewById(R.id.tvName);

        }
    }

    @NonNull
    @Override
    public PersonAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_items, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final User user = data.get(position);
        holder.tvName.setText(user.getUsername());

        if(user.getImageURL().equals("default")){
            holder.civPic.setImageResource(R.mipmap.ic_launcher);
        }
        else{
            Glide.with(mContext).load(user.getImageURL()).into(holder.civPic);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext , MessageActivity.class);
                intent.putExtra("userid" , user.getId());
                mContext.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return data.size();
    }
}


