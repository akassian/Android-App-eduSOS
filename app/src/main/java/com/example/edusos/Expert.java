package com.example.edusos;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Expert implements Parcelable {
    private String id;
    private String name;
    private String googleAccount;
    private String phone;
    private String imgUrl;

    private ArrayList<String> subjects;
    private Double ratePerQuestion = 0.0;
    private int questionsAnswered = 0;
    private Double rating;
    //private Boolean online = false;
    private Boolean online = true;  // for testing

    public Expert() {}
    public Expert (String name, String googleAccount, String phone, ArrayList<String> subjects, Double ratePerQuestion) {
        this.name = name;
        this.googleAccount = googleAccount;
        this.phone = phone;
        this.subjects = subjects;
        this.ratePerQuestion = ratePerQuestion;
    }

    public Expert(Parcel source) {
        id = source.readString();
        name = source.readString();
        googleAccount = source.readString();
        phone = source.readString();
        imgUrl = source.readString();
        subjects = source.readArrayList(String.class.getClassLoader());
        ratePerQuestion = source.readDouble();
        questionsAnswered = source.readInt();
        rating = source.readDouble();
        online = source.readBoolean();

    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGoogleAccount() {
        return googleAccount;
    }

    public void setGoogleAccount(String googleAccount) {
        this.googleAccount = googleAccount;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public ArrayList<String> getSubjects() {
        return subjects;
    }

    public void setSubjects(ArrayList<String> subjects) {
        this.subjects = subjects;
    }

    public Double getRatePerQuestion() {
        return ratePerQuestion;
    }

    public void setRatePerQuestion(Double ratePerQuestion) {
        this.ratePerQuestion = ratePerQuestion;
    }

    public int getQuestionsAnswered() {
        return questionsAnswered;
    }

    public void setQuestionsAnswered(int questionsAnswered) {
        this.questionsAnswered = questionsAnswered;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public Boolean getOnline() {
        return online;
    }

    public void setOnline(Boolean online) {
        this.online = online;
    }

    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.imgUrl);
        dest.writeString(this.name);
        dest.writeString(this.googleAccount);
        dest.writeString(this.phone);
        dest.writeList(this.subjects);
        dest.writeDouble(this.ratePerQuestion);
        dest.writeInt(this.questionsAnswered);
        dest.writeDouble(this.rating);
        dest.writeBoolean(this.online);
    }


    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        @Override
        public Expert createFromParcel(Parcel source) {
            return new Expert(source);
        }

        @Override
        public Expert[] newArray(int size) {
            return new Expert[size];
        }

    };


    
}
