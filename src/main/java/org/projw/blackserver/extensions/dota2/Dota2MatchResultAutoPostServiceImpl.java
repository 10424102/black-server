package org.projw.blackserver.extensions.dota2;

import org.springframework.stereotype.Service;
import org.projw.blackserver.models.User;

@Service
public class Dota2MatchResultAutoPostServiceImpl implements Dota2MatchResultAutoPostService {
    @Override
    public void createSteamAccount(Dota2SteamAccount account) {

    }

    @Override
    public void deleteSteamAccount(Dota2SteamAccount account) {

    }

    @Override
    public void setAutoPostForUser(User user, String extra) {

    }

    @Override
    public void unsetAutoPostForUser(User user) {

    }

    @Override
    public boolean hasBindedSteamAccount(User user) {
        return false;
    }
}
