package com.example.edusos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ExpertSearchActivity extends AppCompatActivity {
    private ImageButton searchBoxButton;
    private EditText searchBox;
    private Boolean onlineOnly = false;

    ArrayList<Expert> allExperts;
    ArrayList<String> allExpertKeys;
    DatabaseReference dbExpert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expert_search);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        searchBoxButton = findViewById(R.id.searchBoxButton);
        searchBox = findViewById(R.id.searchBox);
        searchBoxButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchText = searchBox.getText().toString().trim().toLowerCase();
                goToExpertSearchResultActivity(searchText);
            }
        });
        searchBox.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    String searchText = searchBox.getText().toString().trim().toLowerCase();
                    goToExpertSearchResultActivity(searchText);
                    return true;
                }
                return false;
            }
        });
    }
    public void onCheckboxClicked (View view) {
        onlineOnly = !onlineOnly;

    }

    public void goToExpertSearchResultActivity(String searchText) {
        Intent intent = new Intent(this, ExpertSearchResultActivity.class);

        intent.putExtra("searchText", searchText);
        intent.putExtra("onlineOnly", onlineOnly);

        startActivity(intent);
    }
}
