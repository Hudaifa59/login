package com.example.myapplication;

import android.widget.ImageView;

public class Profile {
    private String description;
    private String name;
    private String gender;
    private String phone;
    private ImageView image;

    public Profile(String description, String name, String gender, String phone, ImageView image) {
        this.description = description;
        this.name = name;
        this.gender = gender;
        this.phone = phone;
        this.image = image;
    }

    @Override
    public String toString() {
        return "Profile{" +
                "description='" + description + '\'' +
                ", name='" + name + '\'' +
                ", gender='" + gender + '\'' +
                ", phone='" + phone + '\'' +
                ", image=" + image +
                '}';
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
