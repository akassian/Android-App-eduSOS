package com.example.edusos;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;

import android.os.Bundle;

import java.util.ArrayList;

public class QuestionSearchResultActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private static RecyclerView.Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_search_result);

        Intent intent = getIntent();

        ArrayList<Question> matchedQuestions = intent.getParcelableArrayListExtra("matchedQuestions");
        ArrayList<String> matchQuestionKeys = intent.getStringArrayListExtra("matchQuestionKeys");
        adapter = new QuestionSearchAdapterClass(matchedQuestions, matchQuestionKeys);

        recyclerView = findViewById(R.id.recycleView);
        recyclerView.setAdapter(adapter);
    }


}
