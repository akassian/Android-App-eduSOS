package com.example.edusos;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.TransitionManager;

import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipDrawable;
import com.google.android.material.chip.ChipGroup;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.edusos.EduUtils.stringToURL;

public class ExpertSearchAdapterClass extends RecyclerView.Adapter<ExpertSearchAdapterClass.ViewHolder> {

    ArrayList<Expert> expertList;
    ArrayList<String> expertKeyList;
    private int mExpandedPosition = -1;

    public ExpertSearchAdapterClass(ArrayList<Expert> expertList, ArrayList<String> expertKeyList) {
        this.expertList = expertList;
        this.expertKeyList = expertKeyList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.activity_expert_profile, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        Expert expertObj = expertList.get(position);

        String name = expertObj.getName().replaceFirst(" ", "\n");
        holder.name.setText(name);

        ArrayList<String> subjects = expertObj.getSubjects();

        holder.subjects.removeAllViews();
        if (subjects != null && subjects.size() >0) {
            for (String subject: subjects) {
                holder.addChip(subject);
            }
        }

        if (expertObj.getRating() != null ) {
            holder.rating.setText(expertObj.getRating().toString() + " / 5");
        } else {
            holder.rating.setText("No Rating");
        }
        if (expertObj.getRatePerQuestion() != null ) {
            holder.ratePerQuestion.setText("$ " + expertObj.getRatePerQuestion().toString());
        } else {
            holder.ratePerQuestion.setText("No Charge");
        }


        if (expertObj.getOnline()) {
            holder.onlineIndicator.setImageResource(R.drawable.ic_online);
        } else {
            holder.onlineIndicator.setImageResource(R.drawable.ic_offline);
        }

        holder.questionsAnswered.setText(expertObj.getQuestionsAnswered() + " Answers");

        URL imageURL = stringToURL(expertObj.getImageURL());
        new ProfilePictureDownloadTask(holder.profilePicture).execute(imageURL);

        final boolean isExpanded = position==mExpandedPosition;
        holder.itemView.setActivated(isExpanded);

        if (!isExpanded) {
            holder.emailButton.setVisibility(View.GONE);
            holder.chatButton.setVisibility(View.GONE);
            holder.bottomDivider.setVisibility(View.INVISIBLE);
        } else {
            holder.emailButton.setVisibility(View.VISIBLE);
            holder.chatButton.setVisibility(View.VISIBLE);
            holder.bottomDivider.setVisibility(View.VISIBLE);
        }
        holder.mItemView.findViewById(R.id.collapsed_expert_item).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notifyItemChanged(mExpandedPosition);
                mExpandedPosition = isExpanded ? -1:position;
                notifyItemChanged(position);
            }
        });
        holder.mItemView.findViewById(R.id.chatButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Expert chosenExpert = expertList.get(position);
                Intent intent = new Intent(view.getContext(), ChatActivity.class);
                intent.putExtra("name", chosenExpert.getName());
                intent.putExtra("googleAcc", chosenExpert.getGoogleAccount());
                view.getContext().startActivity(intent);
            }
        });
        holder.mItemView.findViewById(R.id.emailButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Expert chosenExpert = expertList.get(position);
                String email = chosenExpert.getGoogleAccount() + "@gmail.com";
                Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto", email, null));
                intent.putExtra(Intent.EXTRA_SUBJECT, R.string.email_subject_default);
                intent.putExtra(Intent.EXTRA_TEXT, R.string.email_body_default);
                view.getContext().startActivity(Intent.createChooser(intent, "Choose an Email client :"));
            }
        });
    }


    @Override
    public int getItemCount() {
        return expertList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, rating, ratePerQuestion, questionsAnswered;
        ImageView onlineIndicator;
        CircleImageView profilePicture;
        ChipGroup subjects;
        CardView chatButton, emailButton;
        View mItemView, bottomDivider;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mItemView = itemView;
            name = itemView.findViewById(R.id.expert_profile_name);
            subjects = itemView.findViewById(R.id.expert_profile_subjects);
            rating = itemView.findViewById(R.id.expert_profile_rating);
            ratePerQuestion = itemView.findViewById(R.id.expert_profile_ratePerQuestion);
            onlineIndicator = itemView.findViewById(R.id.onlineIndicator);
            profilePicture = itemView.findViewById(R.id.profileImage);
            questionsAnswered = itemView.findViewById(R.id.expert_profile_questionAnswered);

            bottomDivider = itemView.findViewById(R.id.expert_profile_bottom_divider);
            chatButton = itemView.findViewById(R.id.chatButton);
            emailButton = itemView.findViewById(R.id.emailButton);
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
//            chip.setClickable(false);
            subjects.addView(chip);
        }
    }

    private class ProfilePictureDownloadTask extends AsyncTask<URL, Void, Bitmap> {
        ImageView profileImageView;

        public ProfilePictureDownloadTask(ImageView imageView) {
            profileImageView = imageView;
        }

        // Before the tasks execution
        protected void onPreExecute(){

        }

        // Do the task in background/non UI thread
        protected Bitmap doInBackground(URL...urls){
            URL url = urls[0];
            HttpURLConnection connection;

            try{
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream inputStream = connection.getInputStream();
                BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
                Bitmap bmp = BitmapFactory.decodeStream(bufferedInputStream);

                return bmp;
            } catch(IOException e) {
                Log.e("NETWORK", "Image download error");
            }
            return null;
        }

        // When all async task done
        protected void onPostExecute(Bitmap result){
            if(result!=null){
                // Display the downloaded image into ImageView
                profileImageView.setImageBitmap(result);
            } else {
                result = BitmapFactory.decodeResource(profileImageView.getResources(),
                        R.drawable.ic_prof_pic_default);
                profileImageView.setImageBitmap(result);
            }
        }
    }
}
