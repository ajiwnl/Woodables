package com.intprog.woodablesapp;

public class Post {
    private String title;
    private String message;
    private String userName;

    private String status;

    public Post() {
    }

    public Post(String title, String message, String userName, String status) {
        this.title = title;
        this.message = message;
        this.userName = userName;
        this.status = status;
    }

    // Getters and setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getStatus() {return status;}

    public void setStatus(String status) {this.status = status;}
}
