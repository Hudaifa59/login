package com.example.Classes;


import java.util.ArrayList;

public class Post {
    private String image;
    private ArrayList<Comment> comments;
    private ArrayList<String> likes;
    private String caption;
    private String User;

    public Post(String image, String caption, String user) {
        this.image = image;
        this.comments = new ArrayList<Comment>();
        this.likes=new ArrayList<>() ;
        this.caption = caption;
        User = user;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public ArrayList<Comment> getComments() {
        return comments;
    }

    public void setComments(ArrayList<Comment> comments) {
        this.comments = comments;
    }

    public ArrayList<String> getLikes() {
        return likes;
    }

    public void setLikes(ArrayList<String> likes) {
        this.likes = likes;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getUser() {
        return User;
    }

    public void setUser(String user) {
        User = user;
    }

    @Override
    public String toString() {
        return "Post{" +
                "image='" + image + '\'' +
                ", comments=" + comments +
                ", likes=" + likes +
                ", caption='" + caption + '\'' +
                ", User='" + User + '\'' +
                '}';
    }
}
