package com.example.Classes;

import java.util.ArrayList;

public class Reply {
    private String User;
    private String comment;
    private ArrayList<String> likes;

    public Reply() {
    }

    public Reply(String user, String comment, ArrayList<String> likes) {
        User = user;
        this.comment = comment;
        this.likes = likes;
    }

    public Reply(String comment, String user) {
        this.comment = comment;
        this.User=user;
        this.likes=new ArrayList<String>();
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public ArrayList<String> getLikes() {
        return likes;
    }

    public void setLikes(ArrayList<String> likes) {
        this.likes = likes;
    }

    public String getUser() {
        return User;
    }

    public void setUser(String user) {
        User = user;
    }

    @Override
    public String toString() {
        return "Reply{" +
                "User='" + User + '\'' +
                ", comment='" + comment + '\'' +
                ", likes=" + likes +
                '}';
    }
}
