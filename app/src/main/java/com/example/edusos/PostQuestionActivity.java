package com.example.edusos;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipDrawable;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class PostQuestionActivity extends AppCompatActivity {

    private EditText editTextsubject;
    private EditText editTextquestion;
    private CardView postCard;
    private TextView textViewWelcome;
    private EditText tagsBox;
    private ChipGroup tags;

    private DatabaseReference dbQuestions;

    GoogleSignInAccount googleAccount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_question);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        dbQuestions = FirebaseDatabase.getInstance().getReference("question");

        editTextsubject = (EditText) findViewById(R.id.editText);
        editTextquestion = (EditText) findViewById(R.id.editText2);
        textViewWelcome = (TextView) findViewById(R.id.welcome);
        postCard = (CardView) findViewById(R.id.postButton);
        tagsBox = (EditText) findViewById(R.id.tagsBox);
        tags = (ChipGroup) findViewById(R.id.tags);

        editTextsubject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                closeKeyboard();
            }
        });

        editTextquestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                closeKeyboard();
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
                Toast.makeText(
                        PostQuestionActivity.this, "post success", Toast.LENGTH_SHORT).show();
                onPostSuccess();
            }
        });

        tagsBox.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    String tagText = tagsBox.getText().toString().trim().toLowerCase();
                    final Chip entryChip = getChip(tags, tagText);
                    tags.addView(entryChip);
                    tagsBox.setText("");
                    return true;
                }
                return false;
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
            for (int i = 0; i < tags.getChildCount(); i++) {
                Chip thisChip = (Chip) tags.getChildAt(i);
                topics.add(thisChip.getText().toString());
            }
            ArrayList<String> answer = new ArrayList<>();
            Question q = new Question(subject, question, topics, answer);
            dbQuestions.child(id).setValue(q);
        }
    }

    public void closeKeyboard(){
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
    }

    private Chip getChip(final ChipGroup entryChipGroup, String text) {
        final Chip chip = new Chip(this);
        chip.setChipDrawable(ChipDrawable.createFromResource(this, R.xml.chip));
        int paddingDp = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 10,
                getResources().getDisplayMetrics()
        );
        chip.setPadding(paddingDp, paddingDp, paddingDp, paddingDp);
        chip.setText(text);
        chip.setOnCloseIconClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                entryChipGroup.removeView(chip);
            }
        });
        return chip;
    }

    private void onPostSuccess() {
        Intent myIntent = new Intent(this, MainActivity.class);
        startActivity(myIntent);
    }
}
