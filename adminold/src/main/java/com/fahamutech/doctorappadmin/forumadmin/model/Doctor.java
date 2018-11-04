package com.fahamu.tech.chat.forumadmin.model;

public class Doctor {
    private String name;
    private String email;
    private String id;
    private String photo;
    private String phoneNumber;
    private String address;
    private String ref;

    public Doctor(String name, String email, String id, String photo, String phoneNumber, String ref,
                  String address) {
        this.name = name;
        this.email = email;
        this.id = id;
        this.photo = photo;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.ref = ref;
    }

    public Doctor() {

    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
