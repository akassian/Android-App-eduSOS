package com.example.edusos;

import android.util.Log;

import java.net.MalformedURLException;
import java.net.URL;

public class EduUtils {

    public static URL stringToURL(String urlString){
        try{
            URL url = new URL(urlString);
            return url;
        }catch(MalformedURLException e){
            Log.e("URL", "Image url invalid: " + urlString);
        }
        return null;
    }

}
