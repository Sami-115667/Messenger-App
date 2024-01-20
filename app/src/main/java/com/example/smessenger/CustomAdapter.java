package com.example.smessenger;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<Myview> {
    ChatView chatView;ArrayList<Users> userlist;

    public CustomAdapter(ChatView chatView, ArrayList<Users> userlist) {
        this.chatView = chatView;
        this.userlist = userlist;
    }

    @NonNull
    @Override
    public Myview onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        android.view.View view = LayoutInflater.from(chatView).inflate(R.layout.user,parent,false);
        return new Myview(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Myview holder, int position) {
            Users users= userlist.get(position);
            holder.textView.setText(users.name);
            Picasso.get().load(users.profilepic).into(holder.image);


            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(chatView,Message.class);
                    intent.putExtra("namee",users.getName());
                    intent.putExtra("pic",users.getProfilepic());
                    intent.putExtra("id",users.getUserid());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                  //  Toast.makeText(chatView.getApplicationContext(), "gapla",Toast.LENGTH_SHORT).show();
                    chatView.startActivity(intent);

                }
            });
    }

    @Override
    public int getItemCount() {
        return userlist.size();
    }
}
