package com.example.myapplication;

import android.widget.ImageView;

public class Profile {
    private String nickname;
    private String name;
    private String gender;
    private String phone;
    private ImageView image;

    public Profile(String nickname, String name, String gender, String phone, ImageView image) {
        this.nickname = nickname;
        this.name = name;
        this.gender = gender;
        this.phone = phone;
        this.image = image;
    }

    @Override
    public String toString() {
        return "Profile{" +
                "nickname='" + nickname + '\'' +
                ", name='" + name + '\'' +
                ", gender='" + gender + '\'' +
                ", phone='" + phone + '\'' +
                ", image=" + image +
                '}';
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String description) {
        this.nickname = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public ImageView getImage() {
        return image;
    }

    public void setImage(ImageView image) {
        this.image = image;
    }
}