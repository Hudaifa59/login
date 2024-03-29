package com.example.Classes;

import java.util.ArrayList;

public class Comment {
    private String User;
    private String comment;
    private ArrayList<String> like;
    private ArrayList<String> reply;

    public Comment() {
    }

    public Comment(String user, String comment, ArrayList<String> like, ArrayList<String> reply) {
        User = user;
        this.comment = comment;
        this.like = like;
        this.reply = reply;
    }

    public Comment(String comment, String user) {
        this.comment = comment;
        this.User=user;
        this.reply=new ArrayList<String>();
        like=new ArrayList<String>();
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public ArrayList<String> getLike() {
        return like;
    }

    public void setLike(ArrayList<String> like) {
        this.like = like;
    }

    public ArrayList<String> getReply() {
        return reply;
    }

    public void setReply(ArrayList<String> reply) {
        this.reply = reply;
    }

    public String getUser() {
        return User;
    }

    public void setUser(String user) {
        User = user;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "User='" + User + '\'' +
                ", comment='" + comment + '\'' +
                ", like=" + like +
                ", reply=" + reply +
                '}';
    }
}
