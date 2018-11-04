package com.fahamutech.doctorappadmin.model;

import java.io.Serializable;

public class Article implements Serializable {
    private String categoryId;
    private String id;
    private String date;
    private String content;
    private String packageType;
    private String image;
    private String title;

    public Article() {

    }

    public Article(String categoryId, String id, String date, String content, String packageType,
                   String image, String title) {
        this.categoryId = categoryId;
        this.id = id;
        this.date = date;
        this.content = content;
        this.packageType = packageType;
        this.image = image;
        this.title = title;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPackageType() {
        return packageType;
    }

    public void setPackageType(String packageType) {
        this.packageType = packageType;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
