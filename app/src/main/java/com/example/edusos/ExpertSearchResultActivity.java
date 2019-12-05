package com.example.edusos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ExpertSearchResultActivity extends AppCompatActivity {
    
    private RecyclerView recyclerView;
    private static RecyclerView.Adapter adapter;


    ArrayList<Expert> allExperts;
    ArrayList<String> allExpertKeys;
    ArrayList<Expert> matchedExperts;
    ArrayList<String> matchExpertKeys;
    DatabaseReference dbExpert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expert_search_result);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        Intent intent = getIntent();
        String searchText = intent.getStringExtra("searchText");
        final Boolean onlineOnly = intent.getBooleanExtra("onlineOnly", true);
        final String searchText1 = searchText;
        final Boolean onlineOnly1 = onlineOnly;
        dbExpert = FirebaseDatabase.getInstance().getReference("Experts");

        dbExpert.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    allExperts = new ArrayList<>();
                    allExpertKeys = new ArrayList<>();
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        Expert expert = ds.getValue(Expert.class);
                        String key = ds.getKey();
                        allExperts.add(expert);
                        allExpertKeys.add(key);
                    }
                }
                searchExpert(searchText1, onlineOnly1);
                adapter = new ExpertSearchAdapterClass(matchedExperts, matchExpertKeys);
                recyclerView = findViewById(R.id.expertSearchRecycleView);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ExpertSearchResultActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }

        private void searchExpert(String searchText, Boolean onlineOnly) {
            matchedExperts = new ArrayList<>();
            matchExpertKeys = new ArrayList<>();

            Expert expert;
            searchText = searchText.trim().toLowerCase();

            for (int i = 0; i < allExperts.size(); i++) {

                expert = allExperts.get(i);
                if (expert.getSubjects() != null && expert.getSubjects().size() >0) {
                    for (int j=0; j< expert.getSubjects().size(); j++) {
                        if (expert.getSubjects().get(j).toLowerCase().contains(searchText)) {
                            if (!onlineOnly || (onlineOnly && expert.getOnline())) {
                                matchedExperts.add(allExperts.get(i));
                                matchExpertKeys.add(allExpertKeys.get(i));

                            }

                            break;

                        }

                    }
                }

            }

        }

    }


