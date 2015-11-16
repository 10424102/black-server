package com.example.services.impl;

import com.example.daos.UserRepo;
import com.example.exceptions.IllegalTokenException;
import com.example.models.User;
import com.example.services.TokenService;
import com.example.utils.Cryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 用于生成 Token 字符串
 * Token 是由用户ID（int, 4 个字节），经过 AES 加密的base64字符串
 * 为了简单，暂且这样定
 */
@Service
public class TokenServiceImpl implements TokenService {

    @Autowired
    UserRepo userRepo;

    public String generateToken(User user) {
        int id = user.getId();
        byte[] bytes = new byte[]{
                (byte) (id >>> 24),
                (byte) (id >>> 16),
                (byte) (id >>> 8),
                (byte) id};
        return Cryptor.encrypt(bytes);
    }

    public String generateTokenByPhone(String phone) {
        int id = userRepo.findUserIdByphone(phone);
        byte[] bytes = new byte[]{
                (byte) (id >>> 24),
                (byte) (id >>> 16),
                (byte) (id >>> 8),
                (byte) id};
        return Cryptor.encrypt(bytes);
    }

    public User getUser(String token) throws IllegalTokenException {
        return userRepo.findOne(getUserId(token));
    }

    public int getUserId(String token) throws IllegalTokenException {
        byte[] data = Cryptor.decrypt(token);
        if (data == null) throw new IllegalTokenException();
        int id = data[0];
        id = (id << 8) | data[1];
        id = (id << 8) | data[2];
        id = (id << 8) | data[3];
        return id;
    }

    public boolean isAvailable(String token) {
        try {
            getUserId(token);
        } catch (IllegalTokenException e) {
            return false;
        }
        return true;
    }

}
