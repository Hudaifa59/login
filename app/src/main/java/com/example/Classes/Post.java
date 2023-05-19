package com.example.Classes;


import java.util.ArrayList;

public class Post {
    private String image;
    private ArrayList<String> comments;
    private ArrayList<String> likes;
    private String caption;
    private String User;
    private int share;

    public Post(String image, ArrayList<String> comments, ArrayList<String> likes, String caption, String user,int share) {
        this.image = image;
        this.comments = comments;
        this.likes = likes;
        this.caption = caption;
        User = user;
        this.share=share;
    }

    public Post(String image, String caption, String user) {
        this.image = image;
        this.comments = new ArrayList<String>();
        this.likes=new ArrayList<>() ;
        this.caption = caption;
        User = user;
        this.share=0;
    }

    public int getShare() {
        return share;
    }

    public void setShare(int share) {
        this.share = share;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public ArrayList<String> getComments() {
        return comments;
    }

    public void setComments(ArrayList<String> comments) {
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
                ", share=" + share +
                '}';
    }
}
