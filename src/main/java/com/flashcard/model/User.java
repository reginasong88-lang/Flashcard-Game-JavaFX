package com.flashcard.model;

public class User {
    private int id;
    private String username;
    private String password;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
    // Getters and Setters
    public String getUsername() { return username; }
}