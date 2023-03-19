package users;

public class Reply {
    private String User;
    private String comment;
    private int likes;
    public Reply(String comment,String user) {
        this.comment = comment;
        this.User=user;
        this.likes=0;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
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
