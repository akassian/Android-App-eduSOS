package com.example.edusos;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class PostQuestionActivity extends AppCompatActivity {

    private EditText editTextsubject;
    private EditText editTextquestion;
    private CardView postCard;
    private TextView textViewWelcome;

    private DatabaseReference dbQuestions;

    GoogleSignInAccount googleAccount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_question);

        dbQuestions = FirebaseDatabase.getInstance().getReference("question");

        editTextsubject = (EditText) findViewById(R.id.editText);
        editTextquestion = (EditText) findViewById(R.id.editText2);
        textViewWelcome = (TextView) findViewById(R.id.welcome);
        postCard = (CardView) findViewById(R.id.cardView2);

        editTextsubject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeKeyboard();
            }
        });

        editTextquestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeKeyboard();
            }
        });

        googleAccount = ((EduSOSApplication) this.getApplication()).getAccount();

        if (googleAccount != null) {
            //Log.d("SIGNIN_POST_", googleAccount.getDisplayName() + ",   " + googleAccount.getEmail());
            textViewWelcome.setText("Welcome "+ googleAccount.getDisplayName().split(" ")[0] + "!");
        }
        postCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postQuestion();
                Toast.makeText(PostQuestionActivity.this, "post success", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void postQuestion() {
        String subject = editTextsubject.getText().toString().trim();
        String question = editTextquestion.getText().toString().trim();
        if (subject.isEmpty() || question.isEmpty()) {
            Toast.makeText(this, "Please input the subject and the question", Toast.LENGTH_SHORT).show();
        } else {
            String id = dbQuestions.push().getKey();
            ArrayList<String> topics = new ArrayList<>();
            topics.add("topic1");
            topics.add("topic2");
            ArrayList<String> answer = new ArrayList<>();
//            answer.add("answer1");
//
//            answer.add("answer2");

            Question q = new Question(subject, question, topics, answer);
            dbQuestions.child(id).setValue(q);
        }
    }

    public void onBackClick(View button) {
        Intent myIntent = new Intent(this, MainActivity.class);
        this.startActivity(myIntent);
    }

    public void closeKeyboard(){
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
    }
}
