package com.fahamu.tech.chat.forumadmin.model;

public class ChatMessages {

    private String senderId;
    private String message;
    private String time;
    private String messageType;

    public ChatMessages(){

    }
    public ChatMessages(String senderId, String message, String time,String type) {
        this.senderId = senderId;
        this.message = message;
        this.time = time;
        this.messageType=type;
    }

    public String getType() {
        return messageType;
    }

    public void setType(String type) {
        this.messageType = type;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
