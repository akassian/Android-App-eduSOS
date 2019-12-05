package com.example.edusos;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterStudentActivity extends AppCompatActivity {


    private DatabaseReference dbStudents;

    GoogleSignInAccount googleAccount;
    EditText editTextName;
    EditText editTextGoogleAcc;

    RadioGroup radioGroupMembership;
    String membershipType = "";
    private CardView postCard;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_student);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        dbStudents = FirebaseDatabase.getInstance().getReference("Students");

        editTextName = (EditText) findViewById(R.id.editTextName);
        editTextGoogleAcc = (EditText) findViewById(R.id.editTextGoogleAcc);
        radioGroupMembership = (RadioGroup) findViewById(R.id.radioGroupMembership);
        postCard = (CardView) findViewById(R.id.signUpStudentButton);

        postCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerStudent();
                Toast.makeText(RegisterStudentActivity.this, "You are registered!", Toast.LENGTH_SHORT).show();
            }
        });


    }
    public  void onRadioButtonMembership(View view) {

        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.radio_free:
                if (checked)
                    membershipType = "Free";
                break;
            case R.id.radio_premium:
                if (checked)
                    membershipType = "Premium";
                break;
        }
    }

    private void registerStudent() {
        String name = editTextName.getText().toString().trim();
        String googleAcc = editTextGoogleAcc.getText().toString().trim();

        if (name.isEmpty() || googleAcc.isEmpty()) {
            Toast.makeText(this, "Please input name and Google account", Toast.LENGTH_SHORT).show();
        } else {

            String id = dbStudents.push().getKey();
            Student student = new Student(name, googleAcc, membershipType);
            dbStudents.child(id).setValue(student);
        }
    }
    public void onBackClick(View button) {
        Intent myIntent = new Intent(this, MainActivity.class);
        this.startActivity(myIntent);
    }
}
