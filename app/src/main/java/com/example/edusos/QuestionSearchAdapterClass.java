package com.example.edusos;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipDrawable;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;

public class QuestionSearchAdapterClass extends RecyclerView.Adapter<QuestionSearchAdapterClass.ViewHolder> {

    ArrayList<Question> questionList;
    ArrayList<String> questionKeyList;

    public QuestionSearchAdapterClass(ArrayList<Question> questionList, ArrayList<String> questionKeyList) {
        this.questionList = questionList;
        this.questionKeyList = questionKeyList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.question_search_result_card_view, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Question questionObj = questionList.get(position);
        String answerStr = "";
        holder.subject.setText(questionObj.getSubject());
        holder.question.setText(questionObj.getQuestion());
        if (questionObj.getAnswer() != null && questionObj.getAnswer().size() >0) {
            for (String answerItem: questionObj.getAnswer()) {
                answerStr += "Answer: "+ answerItem + "\n";
            }
            answerStr = answerStr.substring(0, answerStr.length() - 1);
            int color = ContextCompat.getColor(holder.itemView.getContext(), R.color.colorBlack);
            holder.answer.setTextColor(color);
        } else {
            answerStr = "No answers yet";
            int color = ContextCompat.getColor(holder.itemView.getContext(), R.color.colorPrimary);
            holder.answer.setTextColor(color);
        }
        holder.answer.setText(answerStr);

        holder.topics.removeAllViews();
        ArrayList<String> topics = questionObj.getTopics();
        if (topics != null && topics.size() > 0) {
            for (String topic: topics) {
                Log.d("CHIP", topic);
                holder.addChip(topic);
            }
        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("RECYCLER_", String.valueOf(position));
                ArrayList<Question> chosenQuestion = new ArrayList<>();
                chosenQuestion.add(questionList.get(position));
                Intent intent = new Intent(view.getContext(), AnswerQuestionActivity.class);
                //intent.putParcelableArrayListExtra("matchedQuestions", questionList); // not needed
                intent.putParcelableArrayListExtra("chosenQuestion", chosenQuestion);
                intent.putExtra("chosenKey", questionKeyList.get(position));
                Log.d("CHOSENKEY_", questionKeyList.get(position));
                view.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return questionList.size();
    }



    class ViewHolder extends RecyclerView.ViewHolder {
        TextView subject, question, answer;
        ChipGroup topics;
        View mItemView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            subject = itemView.findViewById(R.id.question_result_subject);
            question = itemView.findViewById(R.id.question_result_question);
            answer = itemView.findViewById(R.id.question_result_answer);
            topics = itemView.findViewById(R.id.question_list_chips);
            mItemView = itemView;
        }

        private void addChip(String text) {
            Context context = mItemView.getContext();
            final Chip chip = new Chip(context);
            chip.setChipDrawable(ChipDrawable.createFromResource(context, R.xml.chip));
            int paddingDp = (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, 10,
                    context.getResources().getDisplayMetrics()
            );
            chip.setPadding(paddingDp, paddingDp, paddingDp, paddingDp);
            chip.setText(text);
            chip.setCloseIconVisible(false);
            chip.setClickable(false);
            topics.addView(chip);
        }
    }
}
