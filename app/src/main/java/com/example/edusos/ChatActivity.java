package com.example.edusos;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class ChatActivity extends AppCompatActivity {

    private ImageButton SendMsgBtn;
    private EditText InputMsg;

    private String SenderID;
    private String ReceiverID;
    private String ReceiverName;

    private final List<Messages> messagesList = new ArrayList<>();
    private LinearLayoutManager linearLayoutManager;
    private MessagesAdapterClass messagesAdapter;
    private RecyclerView userMsgList;
    private TextView ReceiverView;

    private DatabaseReference rootRef;

    GoogleSignInAccount account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        Intent intent = getIntent();
        ReceiverName = intent.getStringExtra("name");
        ReceiverID = intent.getStringExtra("googleAcc");
        account = GoogleSignIn.getLastSignedInAccount(this);
        if (account == null) {
            Toast.makeText(this, "seems you don't log in", Toast.LENGTH_SHORT).show();
        } else {
            SenderID = account.getEmail().toString().split("@")[0];
        }

        ReceiverView = findViewById(R.id.chat_user_name);
        ReceiverView.setText(ReceiverName);

        SenderID = SenderID.replaceAll("\\.", "");
        ReceiverID = ReceiverID.replaceAll("\\.", "");


        SendMsgBtn = (ImageButton) findViewById(R.id.send_msg_btn);
        InputMsg = (EditText) findViewById(R.id.input_msg);

        rootRef = FirebaseDatabase.getInstance().getReference();

        SendMsgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendMsg();
            }
        });

        messagesAdapter = new MessagesAdapterClass(messagesList);
        userMsgList = (RecyclerView) findViewById(R.id.msg_list_of_users);
        linearLayoutManager = new LinearLayoutManager(this);
        userMsgList.setLayoutManager(linearLayoutManager);
        userMsgList.setAdapter(messagesAdapter);

    }

    @Override
    protected void onStart() {
        super.onStart();
        rootRef.child("Messages").child(SenderID).child(ReceiverID)
                .addChildEventListener(new ChildEventListener() {

                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        Messages messages = dataSnapshot.getValue(Messages.class);

                        messagesList.add(messages);

                        messagesAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    private void SendMsg() {
        String msg = InputMsg.getText().toString();
        if (msg.isEmpty()) {
            Toast.makeText(this, "plz write your message", Toast.LENGTH_SHORT).show();
        } else {
            String msgSenderRef = "Messages/" + SenderID + "/" + ReceiverID;
            String msgReceiverRef = "Messages/" + ReceiverID + "/" + SenderID;
            DatabaseReference userMsgKeyRef = rootRef.child("Messages")
                    .child(SenderID).child(ReceiverID).push();
            String msgPushID = userMsgKeyRef.getKey();
            Map msgTextBody = new HashMap();
            msgTextBody.put("message", msg);
            msgTextBody.put("type", "text");
            msgTextBody.put("from", SenderID);
            msgTextBody.put("to", ReceiverID);

            Map msgBodyDetail = new HashMap();
            msgBodyDetail.put(msgSenderRef + "/" + msgPushID, msgTextBody);
            msgBodyDetail.put(msgReceiverRef + '/' + msgPushID, msgTextBody);

            rootRef.updateChildren(msgBodyDetail).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(ChatActivity.this, "Msg Sent", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ChatActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                    InputMsg.setText("");
                }
            });

            // update Chat DB
            final DatabaseReference chatRef = rootRef.child("Chat").child(SenderID);
            Query query = chatRef.orderByChild("chatPartner").equalTo(ReceiverID);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (!dataSnapshot.exists()) {
                        DatabaseReference chatPushRef = chatRef.push();

                        String chatSenderRef = "Chat/" + SenderID;
                        String chatReceiverRef = "Chat/" + ReceiverID;

                        String chatPushID = chatPushRef.getKey();
                        Map chatBody = new HashMap();
                        chatBody.put("userID", SenderID);
                        chatBody.put("chatPartner", ReceiverID);
                        Map chat = new HashMap();
                        chat.put(chatSenderRef + "/" + chatPushID, chatBody);
                        chat.put(chatReceiverRef + "/" + chatPushID, chatBody);
                        rootRef.updateChildren(chat);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });





        }
    }
}
