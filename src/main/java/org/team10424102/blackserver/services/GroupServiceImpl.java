package org.team10424102.blackserver.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.team10424102.blackserver.daos.GroupRepo;

@Service
public class GroupServiceImpl implements GroupService {
    @Autowired GroupRepo groupRepo;

    @Override
    public boolean isUserInGroup(int uid, long gid) {
        return groupRepo.isUserInGroup(uid, gid);
    }
}
