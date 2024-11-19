package com.t_t_talk.DB.Models;

public class User {
    long id;
    String hashed_password;
    String username;

    public User(String username, String hashed_password, long id) {
        this.id = id;
        this.username = username;
        this.hashed_password = hashed_password;
    }

    public long getId() {
        return this.id;
    }

    public String getUsername() {
        return this.username;
    }

    public String getHashPW() {
        return this.hashed_password;
    }
}
