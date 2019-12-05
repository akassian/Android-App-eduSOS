package com.example.edusos;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class MessagesAdapterClass extends RecyclerView.Adapter<MessagesAdapterClass.msgViewHolder> {

    private List<Messages> msgList;
    GoogleSignInAccount account;
    private DatabaseReference databaseReference;

    public MessagesAdapterClass(List<Messages> msgList) {
        this.msgList = msgList;
    }

    public class msgViewHolder extends RecyclerView.ViewHolder {

        public TextView senderMsg, receiverMsg;

        public msgViewHolder(@NonNull View itemView) {
            super(itemView);

            senderMsg = (TextView) itemView.findViewById(R.id.sender_msg);
            receiverMsg = (TextView) itemView.findViewById(R.id.receiver_msg);
        }
    }


    @NonNull
    @Override
    public msgViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.per_msg_layout, parent, false);
        account = GoogleSignIn.getLastSignedInAccount(parent.getContext());
        return new msgViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull msgViewHolder holder, int position) {
        String msgSenderID = account.getEmail().toString().split("@")[0];
        msgSenderID = msgSenderID.replaceAll("\\.", "");
        Messages messages = msgList.get(position);
        String fromUserID = messages.getFrom();
        String fromMsgType = messages.getType();

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Messages").child(fromUserID);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        if (fromMsgType.equals("text")) {
            if (fromUserID.equals(msgSenderID)) {

                holder.senderMsg.setVisibility(View.VISIBLE);
                holder.receiverMsg.setVisibility(View.INVISIBLE);
                holder.senderMsg.setTextColor(Color.BLACK);
                holder.senderMsg.setBackgroundResource(R.drawable.sender_msg_layout);
                holder.senderMsg.setText(messages.getMessage());
            } else {
                holder.senderMsg.setVisibility(View.INVISIBLE);
                holder.receiverMsg.setVisibility(View.VISIBLE);
                holder.receiverMsg.setBackgroundResource(R.drawable.receiver_msg_layout);
                holder.receiverMsg.setTextColor(Color.BLACK);
                holder.receiverMsg.setText(messages.getMessage());
            }
        }

    }

    @Override
    public int getItemCount() {
        return msgList.size();
    }
}
