package com.example.services;

import com.example.exceptions.IllegalTokenException;
import com.example.models.User;

public interface TokenService {
    String generateToken(User user);

    String generateTokenByPhone(String phone);

    User getUser(String token) throws IllegalTokenException;

    int getUserId(String token) throws IllegalTokenException;

    boolean isAvailable(String token);
}
