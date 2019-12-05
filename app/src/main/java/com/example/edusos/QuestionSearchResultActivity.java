package com.example.edusos;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;

import android.os.Bundle;
import android.util.TypedValue;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipDrawable;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;

public class QuestionSearchResultActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private static RecyclerView.Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_search_result);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        Intent intent = getIntent();

        ArrayList<Question> matchedQuestions = intent.getParcelableArrayListExtra("matchedQuestions");
        ArrayList<String> matchQuestionKeys = intent.getStringArrayListExtra("matchQuestionKeys");
        adapter = new QuestionSearchAdapterClass(matchedQuestions, matchQuestionKeys);

        recyclerView = findViewById(R.id.questionSearchRecycleView);
        recyclerView.setAdapter(adapter);
    }
}
