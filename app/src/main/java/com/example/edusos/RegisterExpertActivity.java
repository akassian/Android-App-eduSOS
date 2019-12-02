package com.example.edusos;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class RegisterExpertActivity extends AppCompatActivity {
    private DatabaseReference dbExperts;

    GoogleSignInAccount googleAccount;
    EditText editTextName;
    EditText editTextGoogleAcc;
    EditText editTextPhone;
    EditText editTextSubjects;
    EditText editTextRatePerQuestion;
    private CardView postCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_expert);
        dbExperts = FirebaseDatabase.getInstance().getReference("Experts");

        editTextName = (EditText) findViewById(R.id.editTextName);
        editTextGoogleAcc = (EditText) findViewById(R.id.editTextGoogleAcc);
        editTextPhone = (EditText) findViewById(R.id.editTextPhone);
        editTextSubjects = (EditText) findViewById(R.id.editTextSubjects);
        editTextRatePerQuestion = (EditText) findViewById(R.id.editTextRatePerQuestion);
        postCard = (CardView) findViewById(R.id.cardView2);

        postCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerExpert();
                Toast.makeText(RegisterExpertActivity.this, "You are registered!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void registerExpert() {
        String name = editTextName.getText().toString().trim();
        String googleAcc = editTextGoogleAcc.getText().toString().trim();
        String phone = editTextPhone.getText().toString().trim();
        String subjectsStr = editTextSubjects.getText().toString().trim();
        Double ratePerQ = Double.parseDouble( editTextRatePerQuestion.getText().toString().trim());

        if (name.isEmpty() || googleAcc.isEmpty() || subjectsStr.isEmpty()) {
            Toast.makeText(this, "Please input name, Google account, and subjects", Toast.LENGTH_SHORT).show();
        } else {
            ArrayList<String> subjects = new ArrayList<>();
            String [] subjectArr = subjectsStr.split(",");
            for ( int i = 0; i <subjectArr.length; i++) {
                subjects.add(subjectArr[i].trim());
            }
            String id = dbExperts.push().getKey();
            Expert expert = new Expert(name, googleAcc, phone,subjects,ratePerQ);
            dbExperts.child(id).setValue(expert);
        }
    }


    public void onBackClick(View button) {
        Intent myIntent = new Intent(this, MainActivity.class);
        this.startActivity(myIntent);
    }

}
