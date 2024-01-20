
        package com.example.smessenger;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smessenger.Msgmodel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class Message extends AppCompatActivity {

    String nameee, pic, senderUid, Receiverid;
    CircleImageView profile;
    TextView name;
    EditText message;
    CardView send;
    FirebaseAuth auth;
    FirebaseDatabase database;
    public static String senderimage, receiverimage;
    String SenderRoom, ReceiverRoom;
    RecyclerView recyclerView;
    ArrayList<Msgmodel> messagesarraylist;
    MessageAdapter messageAdapter;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        messagesarraylist = new ArrayList<>();

        recyclerView = findViewById(R.id.recycleviewerinmessage);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        messageAdapter = new MessageAdapter(getApplicationContext(), messagesarraylist);
        recyclerView.setAdapter(messageAdapter);

        nameee = getIntent().getStringExtra("namee");
        pic = getIntent().getStringExtra("pic");
        Receiverid = getIntent().getStringExtra("id");

        message = findViewById(R.id.textmessage);
        send = findViewById(R.id.send);

        profile = findViewById(R.id.profileimageinmessage);
        name = findViewById(R.id.nameinmessage);
        name.setText(nameee);
        Picasso.get().load(pic).into(profile);

        auth = FirebaseAuth.getInstance();
        senderUid = auth.getUid();

        SenderRoom = senderUid + Receiverid;
        ReceiverRoom = Receiverid + senderUid;

        database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference().child("User").child(senderUid);

        DatabaseReference chatreference = database.getReference().child("Chat");

        chatreference.child(SenderRoom).child("Message").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messagesarraylist.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Msgmodel message = dataSnapshot.getValue(Msgmodel.class);
                    messagesarraylist.add(message);
                }
                messageAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle onCancelled if needed
            }
        });

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                receiverimage = pic;
                senderimage = snapshot.child("profilepic").getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle onCancelled if needed
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mess = message.getText().toString();
                if (mess.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Enter the message", Toast.LENGTH_SHORT).show();
                } else {
                    message.setText("");

                    Date date = new Date();
                    Msgmodel msg = new Msgmodel(mess, senderUid, date.getTime());
                    DatabaseReference senderReference = database.getReference().child("Chat").child(SenderRoom).child("Message").push();
                    senderReference.setValue(msg).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            DatabaseReference receiverReference = database.getReference().child("Chat").child(ReceiverRoom).child("Message").push();
                            receiverReference.setValue(msg);
                        }
                    });
                }
            }
        });
    }
}
