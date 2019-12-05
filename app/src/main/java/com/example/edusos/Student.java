package com.example.edusos;

import android.os.Parcel;
import android.os.Parcelable;

public class Student implements Parcelable {
    private String id;
    private String name;
    private String googleAccount;
    private String membershipType;
    private Boolean online = true;

    public Student() {}
    public Student (String name, String googleAccount, String membershipType) {
        this.name = name;
        this.googleAccount = googleAccount;
        this.membershipType = membershipType;
    }

    public Student(Parcel source) {
        id = source.readString();
        name = source.readString();
        googleAccount = source.readString();
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

    public String getMembershipType() {
        return membershipType;
    }

    public void setMembershipType(String membershipType) {
        this.membershipType = membershipType;
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
        dest.writeString(this.name);
        dest.writeString(this.googleAccount);
        dest.writeBoolean(this.online);
    }


    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        @Override
        public Student createFromParcel(Parcel source) {
            return new Student(source);
        }

        @Override
        public Student[] newArray(int size) {
            return new Student[size];
        }

    };

}
