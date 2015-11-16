package com.example.services;

import com.example.models.User;
import org.jetbrains.annotations.NotNull;

public interface UserService {

    @NotNull
    User getCurrentUser();

    int getCurrentUserId();

    void addUser(int userId);

    boolean currentUserIsHisFriend(int UserId);

    boolean currentUserIsHisFan(int UserId);

    boolean currentUserIsHisFollwing(int UserId);
}
