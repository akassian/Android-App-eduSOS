package com.example.edusos;

public class Chat {
    private String userID;
    private String chatPartner;
    private String partnerImage;
    private String partnerName;
    private String senderName;
    private boolean partnerOnlineStatus;

    public Chat(String userID, String chatPartner, String partnerImage, boolean partnerOnlineStatus, String partnerName, String userName) {
        this.userID = userID;
        this.chatPartner = chatPartner;
        this.partnerImage = partnerImage;
        this.partnerOnlineStatus = partnerOnlineStatus;
        this.partnerName = partnerName;
        this.senderName = senderName;
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

    public String getPartnerImage() {
        return partnerImage;
    }

    public void setPartnerImage(String url) {
        this.partnerImage = url;
    }

    public boolean getPartnerOnlineStatus() {
        return partnerOnlineStatus;
    }

    public void setPartnerOnlineStatus(boolean status) {
        this.partnerOnlineStatus = status;
    }

    public String getPartnerName() {
        return partnerName;
    }

    public void setPartnerName(String name) {
        this.partnerName = name;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String name) {
        this.senderName = name;
    }
}
