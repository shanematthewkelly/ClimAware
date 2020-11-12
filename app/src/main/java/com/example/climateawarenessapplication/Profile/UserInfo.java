package com.example.climateawarenessapplication.Profile;

public class UserInfo {

    private String phone;
    private String user;
    private String username;


    public UserInfo(String userID, String username) {

        this.user = userID;
        this.user = username;

    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}


