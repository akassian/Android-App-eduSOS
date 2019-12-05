package com.example.edusos;

public class Messages {
    private String from, to, message, type;

    public Messages(String message, String type, String from, String to) {
        this.message = message;
        this.type = type;
        this.from = from;
        this.to = to;
    }

    public Messages() {}

    public String getFrom() {
        return from;
    }

    public String getMessage() {
        return message;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getTo() {
        return to;
    }

    public String getType() {
        return type;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setType(String type) {
        this.type = type;
    }
}
