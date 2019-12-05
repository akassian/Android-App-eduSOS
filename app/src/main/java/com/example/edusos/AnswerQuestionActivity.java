package com.example.edusos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipDrawable;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class AnswerQuestionActivity extends AppCompatActivity {
    DatabaseReference dbQuestion;
    Question questionObj;
    EditText answerInput;
    String key;
    GoogleSignInAccount googleAccount;
    private TextView previousAnswers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer_question);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        dbQuestion = FirebaseDatabase.getInstance().getReference("question");

        TextView subject = (TextView) findViewById(R.id.cardview_subject1);
        TextView question = (TextView) findViewById(R.id.cardview_question1);
        previousAnswers = (TextView) findViewById(R.id.cardview_previous_answer1);
        TextView welcome = (TextView) findViewById(R.id.cardview_welcome1);
        answerInput = (EditText) findViewById(R.id.cardview_answer1);
        ChipGroup chipGroup = (ChipGroup) findViewById(R.id.answer_question_chips);

        Intent intent = getIntent();

        ArrayList<Question> chosenQuestion = intent.getParcelableArrayListExtra("chosenQuestion");
        questionObj = chosenQuestion.get(0);
        key = intent.getStringExtra("chosenKey");
        Log.d("KEY_", key);
        subject.setText(questionObj.getSubject());
        question.setText(questionObj.getQuestion());
        String answerStr = "";
        if (questionObj.getAnswer() != null && questionObj.getAnswer().size() >0) {
            for (String answerItem: questionObj.getAnswer()) {
                answerStr += "Answer: "+ answerItem + "\n\n";
            }
            answerStr = answerStr.substring(0, answerStr.length() - 2);
        } else {
            answerStr = "No answers yet";
        }
        previousAnswers.setText(answerStr);

        ArrayList<String> topics = questionObj.getTopics();
        if (topics != null && topics.size() >0) {
            for (String topic: topics) {
                Chip subjectChip = getChip(topic);
                chipGroup.addView(subjectChip);
            }
        }

        googleAccount = ((EduSOSApplication) this.getApplication()).getAccount();

        if (googleAccount != null) {
            Log.d("SIGNIN_POST_", googleAccount.getDisplayName() + ",   " + googleAccount.getEmail());
            if (((EduSOSApplication) AnswerQuestionActivity.this.getApplication()).getLoggedInAsExpert())
            {
                welcome.setText("Signed in as " + googleAccount.getDisplayName().split(" ")[0] + " (Expert)");
            } else {
                welcome.setText("Signed in as " + googleAccount.getDisplayName().split(" ")[0]);
            }

        }

    }

    public void onSubmit (View view) {
        ArrayList<String> answer = questionObj.getAnswer();
        if (answer == null) {
            answer = new ArrayList<String>();
        }
        String answerStr = answerInput.getText().toString();
        answer.add(answerStr);

        DatabaseReference updateData = FirebaseDatabase.getInstance()
                .getReference("question")
                .child(key);
        updateData.child("answer").setValue(answer);
        String answersStr = "";
        for (String answerItem: answer) {
            answersStr += "Answer: "+ answerItem + "\n\n";
        }
        answersStr = answersStr.substring(0, answersStr.length() - 2);
        previousAnswers.setText(answersStr);
        answerInput.setText("");
    }

    private Chip getChip(String text) {
        final Chip chip = new Chip(this);
        chip.setChipDrawable(ChipDrawable.createFromResource(this, R.xml.chip));
        int paddingDp = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 10,
                getResources().getDisplayMetrics()
        );
        chip.setPadding(paddingDp, paddingDp, paddingDp, paddingDp);
        chip.setText(text);
        chip.setCloseIconVisible(false);
        chip.setClickable(false);
        return chip;
    }
}
