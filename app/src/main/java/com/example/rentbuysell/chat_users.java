package com.example.rentbuysell;

public class chat_users {
    private String user_id;
    private String username;
    private String imageUrl;
    public chat_users()
    {}

    public chat_users(String user_id, String username, String imageUrl) {
        this.user_id = user_id;
        this.username = username;
        this.imageUrl = imageUrl;
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
}
