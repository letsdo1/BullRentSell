package com.codingnew.rentbuysell;

public class chat_users {
    private String user_id;
    private String username;
    private String imageUrl;
    private String status;
    public chat_users()
    { }

    public chat_users(String user_id, String username, String imageUrl,String status) {
        this.user_id = user_id;
        this.username = username;
        this.imageUrl = imageUrl;
        this.status=status;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getUsername() {
        return username;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
