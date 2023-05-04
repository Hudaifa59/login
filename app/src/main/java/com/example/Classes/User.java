package com.example.Classes;

import java.util.ArrayList;

public class User {
    private String username;
    private ArrayList<String> following;
    private ArrayList<String> followers;
    private String profile;
    private String user;

    public User(String username, ArrayList<String> following, ArrayList<String> followers, String profile, String user) {
        this.username = username;
        this.following = following;
        this.followers = followers;
        this.profile = profile;
        this.user = user;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public ArrayList<String> getFollowing() {
        return following;
    }

    public void setFollowing(ArrayList<String> following) {
        this.following = following;
    }

    public ArrayList<String> getFollowers() {
        return followers;
    }

    public void setFollowers(ArrayList<String> followers) {
        this.followers = followers;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", following=" + following +
                ", followers=" + followers +
                ", profile='" + profile + '\'' +
                ", user='" + user + '\'' +
                '}';
    }
}
