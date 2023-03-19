package users;

import android.widget.ImageView;

public class Profile {
    private String nickname;
    private String name;
    private String gender;
    private String phone;
    private String image;
    private int posts;


    public Profile(String nickname, String name, String gender, String phone,String image) {
        this.nickname = nickname;
        this.name = name;
        this.gender = gender;
        this.phone = phone;
        this.image=image;
        this.posts=0;
    }

    @Override
    public String toString() {
        return "Profile{" +
                "nickname='" + nickname + '\'' +
                ", name='" + name + '\'' +
                ", gender='" + gender + '\'' +
                ", phone='" + phone + '\'' +
                ", image='" + image + '\'' +
                ", posts=" + posts +
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
    public String getImage() {
        return image;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public void setImage(String image) {
        this.phone = image;
    }

    public void setPosts(int posts) {
        this.posts = posts;
    }

    public int getPosts() {
        return posts;
    }
}
