package com.example.edusos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import com.google.android.gms.auth.api.signin.GoogleSignIn;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;






public class MainActivity extends AppCompatActivity {

    private ImageButton searchBoxButton;
    private EditText searchBox;
    private Button postButton;

    ArrayList<Question> allQuestions;
    ArrayList<String> allQuestionKeys;
    DatabaseReference dbQuestion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchBoxButton = findViewById(R.id.searchBoxButton);
        searchBox = findViewById(R.id.searchBox);
        searchBoxButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchText = searchBox.getText().toString().trim().toLowerCase();
                searchQuestion(searchText);
            }
        });
        postButton = findViewById(R.id.postButton);
        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                openPostQuestionActivity();
            }
        });

        dbQuestion = FirebaseDatabase.getInstance().getReference("question");
        dbQuestion.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    allQuestions = new ArrayList<>();
                    allQuestionKeys = new ArrayList<>();
                    for (DataSnapshot ds: dataSnapshot.getChildren()) {
                        Question question = ds.getValue(Question.class);
                        String key = ds.getKey();
                        allQuestions.add(question);
                        allQuestionKeys.add(key);
                    }
//                    QuestionSearchAdapterClass questionSearchAdapterClass = new QuestionSearchAdapterClass(allQuestions);
//                    openQuestionSearchResultActivity(questionSearchAdapterClass);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MainActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void searchQuestion(String searchText) {
        ArrayList<Question> matchedQuestions = new ArrayList<>();
        ArrayList<String> matchQuestionKeys = new ArrayList<>();
        Boolean match;
        Question question;
        //for (Question question: allQuestions) {
        for (int i = 0; i < allQuestions.size(); i++) {
            match = Boolean.FALSE;
            question = allQuestions.get(i);
            if (question.getQuestion().toLowerCase().contains(searchText)) {
                matchedQuestions.add(allQuestions.get(i));
                matchQuestionKeys.add(allQuestionKeys.get(i));
                        match = Boolean.TRUE;
            }
            if (!match && question.getAnswer() != null && question.getAnswer().size() > 0) {
                for (int j = 0; j < question.answer.size(); j++) {
                    if (!match && question.getAnswer().get(j).toLowerCase().contains(searchText)) {
                        matchedQuestions.add(question);
                        match = Boolean.TRUE;
                    }
                }
                if (match == Boolean.TRUE) {
                    matchQuestionKeys.add(allQuestionKeys.get(i));
                }
            }
        }
        openQuestionSearchResultActivity(matchedQuestions, matchQuestionKeys);
    }



    public void openPostQuestionActivity() {
        Intent intent = new Intent(this, PostQuestionActivity.class);
        startActivity(intent);
    }

    public void onPostClick(View button) {
        Intent myIntent = new Intent(this, PostQuestionActivity.class);
        this.startActivity(myIntent);
    }

    public void openQuestionSearchResultActivity(ArrayList<Question> matchedQuestions, ArrayList<String> matchQuestionKeys) {
        Intent intent = new Intent(this, QuestionSearchResultActivity.class);
        intent.putParcelableArrayListExtra("matchedQuestions", matchedQuestions);
        intent.putStringArrayListExtra("matchQuestionKeys", matchQuestionKeys);

        startActivity(intent);
    }

    public void onLogInSignUpClick(View view) {
        Intent myIntent = new Intent(this, LogInSignUpActivity.class);
        this.startActivity(myIntent);

    }
}
