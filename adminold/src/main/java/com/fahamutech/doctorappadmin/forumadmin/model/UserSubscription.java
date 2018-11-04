package com.fahamu.tech.chat.forumadmin.model;

public class UserSubscription {
    private String amount;
    private String userId;
    private String start;
    private String end;

    public UserSubscription(){

    }

    public UserSubscription(String amount, String userId, String start, String end) {
        this.amount = amount;
        this.userId = userId;
        this.start = start;
        this.end = end;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }
}
