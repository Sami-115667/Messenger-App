package com.example.smessenger;

import static com.example.smessenger.Message.receiverimage;
import static com.example.smessenger.Message.senderimage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageAdapter extends RecyclerView.Adapter {
    Context context;
    ArrayList<Msgmodel>messagemodelarraylist;

    public MessageAdapter(Context context, ArrayList<Msgmodel> messagemodelarraylist) {
        this.context = context;
        this.messagemodelarraylist = messagemodelarraylist;
    }

    int ITEM_Send=1,ITEM_Receive=2;
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            if(viewType ==ITEM_Send){
                View v= LayoutInflater.from(context).inflate(R.layout.sendmessage,parent,false);
                return  new SenderViewholder(v);
            }

        if(viewType ==ITEM_Receive){
            View v= LayoutInflater.from(context).inflate(R.layout.recevemessage,parent,false);
            return  new ReceiverViewholder(v);
        }


        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Msgmodel message= messagemodelarraylist.get(position);
        if(holder.getClass()==SenderViewholder.class){
            SenderViewholder viewholder= (SenderViewholder) holder;
            viewholder.textViewsender.setText(message.getMessage());
            Picasso.get().load(senderimage).into(viewholder.circleImageViewsender);
        }
        else{
            ReceiverViewholder viewholder=(ReceiverViewholder) holder;
            viewholder.textViewreceiver.setText(message.getMessage());
            Picasso.get().load(receiverimage).into(viewholder.circleImageViewreceiver);

        }

    }

    @Override
    public int getItemCount() {
        return messagemodelarraylist.size();
    }

    @Override
    public int getItemViewType(int position) {
        Msgmodel message = messagemodelarraylist.get(position);
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        if (message.getSenderid().equals(currentUserId)) {
            return ITEM_Send;
        } else {
            return ITEM_Receive;
        }
    }


    class SenderViewholder extends RecyclerView.ViewHolder{

        CircleImageView circleImageViewsender;
        TextView textViewsender;

        public SenderViewholder(@NonNull View itemView) {
            super(itemView);
            circleImageViewsender=itemView.findViewById(R.id.senderpic  );
            textViewsender=itemView.findViewById(R.id.sendermessage);
        }
    }

    class ReceiverViewholder extends RecyclerView.ViewHolder{

        CircleImageView circleImageViewreceiver;
        TextView textViewreceiver;
        public ReceiverViewholder(@NonNull View itemView) {
            super(itemView);
            circleImageViewreceiver=itemView.findViewById(R.id.receiverpic);
            textViewreceiver=itemView.findViewById(R.id.receivermessage);
        }
    }
}
