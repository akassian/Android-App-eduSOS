package com.example.edusos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import android.view.View;

import org.w3c.dom.Text;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ChatListActivity extends AppCompatActivity {

    private RecyclerView chatListRecyclerView;
    private DatabaseReference databaseReference;
    GoogleSignInAccount account;
    private String userAccount, curUserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        chatListRecyclerView = findViewById(R.id.chatListRecyclerView);
        chatListRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        account = GoogleSignIn.getLastSignedInAccount(this);
        if (account == null) {
            Toast.makeText(this, "You must Log in first", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        } else {
            userAccount = account.getEmail().toString().split("@")[0];
        }
        curUserID = userAccount.replaceAll("\\.", "");
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Chat").child(curUserID);

    }

    @Override
    protected void onStart() {
        super.onStart();
        final boolean isExpert = ((EduSOSApplication) getApplication()).getLoggedInAsExpert();
        final GoogleSignInAccount googleAccount = ((EduSOSApplication) getApplication()).getAccount();
        FirebaseRecyclerOptions<Chat> options = new FirebaseRecyclerOptions.Builder<Chat>()
                .setQuery(databaseReference, Chat.class)
                .build();

        FirebaseRecyclerAdapter<Chat, chatViewHolder> adapter =
                new FirebaseRecyclerAdapter<Chat, chatViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final chatViewHolder chatViewHolder, int i, @NonNull Chat chat) {
                final String userID = chat.getUserID();
                final String chatPartner = chat.getChatPartner();
                final String imageURLstring = chat.getPartnerImage();
                final boolean partnerStatus = chat.getPartnerOnlineStatus();
                final String partnerName;
                final String userName = chat.getSenderName();

                if (chat.getPartnerName() != null) {
                    partnerName = chat.getPartnerName();
                } else {
                    partnerName = chatPartner;
                }

                if (!isExpert) {
                    chatViewHolder.userName.setText(partnerName);
                    if (partnerStatus) {
                        chatViewHolder.onlineStatus.setImageResource(R.drawable.ic_online);
                    } else {
                        chatViewHolder.onlineStatus.setImageResource(R.drawable.ic_offline);
                    }

                    if (imageURLstring == null || imageURLstring.equals("")) {
                        chatViewHolder.profileImage.setVisibility(View.INVISIBLE);
                        chatViewHolder.onlineStatus.setVisibility(View.GONE);
                    } else {
                        URL imageURL = EduUtils.stringToURL(imageURLstring);
                        new ProfilePictureDownloadTask(chatViewHolder.profileImage).execute(imageURL);
                    }
                } else {
                    chatViewHolder.profileImage.setVisibility(View.INVISIBLE);
                    chatViewHolder.onlineStatus.setVisibility(View.GONE);
                    chatViewHolder.userName.setText(userName);
                }

                chatViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(v.getContext(), ChatActivity.class);
                        if (userID.equals(curUserID)) {
                            if (isExpert) {
                                intent.putExtra("name", userName);
                                intent.putExtra("imageURL", "");
                            } else {
                                intent.putExtra("name", partnerName);
                                intent.putExtra("imageURL", imageURLstring);
                            }
                            intent.putExtra("googleAcc", chatPartner);
                            intent.putExtra("online", partnerStatus);
                        } else {
                            intent.putExtra("name", partnerName);
                            intent.putExtra("googleAcc", userID);
                            intent.putExtra("imageURL", imageURLstring);
                            if (isExpert) {
                                intent.putExtra("name", userName);
                                intent.putExtra("imageURL", "");

                            } else {
                                intent.putExtra("name", partnerName);
                                intent.putExtra("imageURL", imageURLstring);
                            }
                        }

                        startActivity(intent);
                    }
                });
            }

            @NonNull
            @Override
            public chatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_list_card_view, parent, false);
                return new chatViewHolder(view);
            }
        };

        chatListRecyclerView.setAdapter(adapter);
        adapter.startListening();

    }

    public static class chatViewHolder extends RecyclerView.ViewHolder {

        TextView userName;
        ImageView profileImage;
        ImageView onlineStatus;

        public chatViewHolder(View view) {
            super(view);
            userName = view.findViewById(R.id.chatListCardView_Name);
            profileImage = view.findViewById(R.id.chat_list_profileImage);
            onlineStatus = view.findViewById(R.id.chat_list_onlineIndicator);
        }
    }
}
