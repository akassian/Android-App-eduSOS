package com.example.edusos;

public class Chat {
    private String userID;
    private String chatPartner;

    public Chat(String userID, String chatPartner) {
        this.userID = userID;
        this.chatPartner = chatPartner;
    }

    public Chat() {}

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getChatPartner() {
        return chatPartner;
    }

    public void setChatPartner(String chatPartner) {
        this.chatPartner = chatPartner;
    }
}
