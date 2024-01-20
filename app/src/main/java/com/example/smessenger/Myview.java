package com.example.smessenger;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import de.hdodenhof.circleimageview.CircleImageView;

public class Myview extends RecyclerView.ViewHolder {

    CircleImageView image;
    TextView textView;
    public Myview(@NonNull View itemView) {
        super(itemView);
        image=itemView.findViewById(R.id.userimageforrecycler);
        textView=itemView.findViewById(R.id.usernameforrecycler );
    }
}
