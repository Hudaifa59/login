package users;

import java.util.ArrayList;

public class Comment {
    private String User;
    private String comment;
    private int like;
    private ArrayList<Reply> reply;

    public Comment(String comment,String user) {
        this.comment = comment;
        this.User=user;
        this.reply=new ArrayList<Reply>();
        like=0;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getLike() {
        return like;
    }

    public void setLike(int like) {
        this.like = like;
    }

    public ArrayList<Reply> getReply() {
        return reply;
    }

    public void setReply(ArrayList<Reply> reply) {
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
