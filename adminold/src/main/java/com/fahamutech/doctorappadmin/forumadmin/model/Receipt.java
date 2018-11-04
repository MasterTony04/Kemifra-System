package com.fahamu.tech.chat.forumadmin.model;

public class Receipt {
    private String start;
    private String amount;
    private String userId;
    private String end;

    public Receipt(){

    }

    public Receipt(String start, String amount, String userId, String end) {
        this.start = start;
        this.amount = amount;
        this.userId = userId;
        this.end = end;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
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

    public String getReference() {
        return end;
    }

    public void setReference(String reference) {
        this.end = reference;
    }
}
