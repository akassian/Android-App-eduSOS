package com.example.edusos;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

public class ExpertSearchAdapterClass extends RecyclerView.Adapter<ExpertSearchAdapterClass.ViewHolder> {

    ArrayList<Expert> expertList;
    //ArrayList<String> ExpertKeyList;

    public ExpertSearchAdapterClass(ArrayList<Expert> expertList) {
        this.expertList = expertList;
        //this.ExpertKeyList = ExpertKeyList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.question_search_result_card_view, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Expert expertObj = expertList.get(position);
        //String answerStr = "";
//        holder.subject.setText(questionObj.getSubject());
//        holder.question.setText(questionObj.getExpert());
//        if (questionObj.getAnswer() != null && questionObj.getAnswer().size() >0) {
//            for (String answerItem: questionObj.getAnswer()) {
//                answerStr += "Answer: "+ answerItem + "\n";
//
//            }
//        }
//        holder.answer.setText(answerStr);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("RECYCLER_", String.valueOf(position));
                ArrayList<Expert> chosenExpert = new ArrayList<>();
                chosenExpert.add(expertList.get(position));
                Intent intent = new Intent(view.getContext(), AnswerQuestionActivity.class);
                //intent.putParcelableArrayListExtra("matchedQuestions", expertList); // not needed
//

                view.getContext().startActivity(intent);
            }
        });
    }

//    public void onBindViewHolder(ViewHolder holder, final int position) {
//        holder.view.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(context, "Recycle Click" + position, Toast.LENGTH_SHORT).show();
//            }
//        });
//    }

    @Override
    public int getItemCount() {
        return expertList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView subject, question, answer;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            subject = itemView.findViewById(R.id.cardview_subject);
            question = itemView.findViewById(R.id.cardview_question);
            answer = itemView.findViewById(R.id.cardview_answer);
        }
    }
}
