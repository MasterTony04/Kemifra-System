package com.fahamu.tech.chat.forumadmin.model;

import java.io.Serializable;

public class ChatTopic implements Serializable {

    private String title;
    private String description;
    private String userId;
    private String time;
    private String docId;
    private String userPhoto;
    private boolean userSeen;
    private boolean doctorSeen;

    public ChatTopic() {

    }

    public ChatTopic(
            String title,
            String description,
            String userId,
            String time,
            String userPhoto,
            boolean userSeen,
            boolean doctorSeen) {
        this.title = title;
        this.description = description;
        this.userId = userId;
        this.time = time;
        this.userPhoto = userPhoto;
        this.userSeen = userSeen;
        this.doctorSeen = doctorSeen;
    }

    public boolean isDoctorSeen() {
        return doctorSeen;
    }

    public boolean isUserSeen() {
        return userSeen;
    }

    public void setDoctorSeen(boolean doctorSeen) {
        this.doctorSeen = doctorSeen;
    }

    public void setUserSeen(boolean userSeen) {
        this.userSeen = userSeen;
    }

    public String getUserPhoto() {
        return userPhoto;
    }

    public void setUserPhoto(String userPhoto) {
        this.userPhoto = userPhoto;
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }

    public String getDocId() {
        return docId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
