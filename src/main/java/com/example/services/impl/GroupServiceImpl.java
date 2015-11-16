package com.example.services.impl;

import com.example.daos.GroupRepo;
import com.example.services.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GroupServiceImpl implements GroupService {
    @Autowired GroupRepo groupRepo;

    @Override
    public boolean isUserInGroup(int uid, long gid) {
        return groupRepo.isUserInGroup(uid, gid);
    }
}
