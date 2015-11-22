package com.example.config.security;

public class DomainUser {
    private String username;

    public DomainUser(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public String toString() {
        return username;
    }
}
