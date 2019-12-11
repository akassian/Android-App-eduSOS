package com.example.edusos;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ProfilePictureDownloadTask extends AsyncTask<URL, Void, Bitmap> {
    ImageView profileImageView;

    public ProfilePictureDownloadTask(ImageView imageView) {
        profileImageView = imageView;
    }

    // Before the tasks execution
    protected void onPreExecute(){
        Bitmap blank = BitmapFactory.decodeResource(profileImageView.getResources(),
                R.drawable.ic_prof_pic_default);
        profileImageView.setImageBitmap(blank);
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
