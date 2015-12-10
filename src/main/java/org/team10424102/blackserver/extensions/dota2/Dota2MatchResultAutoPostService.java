package org.team10424102.blackserver.extensions.dota2;

import org.team10424102.blackserver.models.User;

public interface Dota2MatchResultAutoPostService {

    void createSteamAccount(Dota2SteamAccount account);

    void deleteSteamAccount(Dota2SteamAccount account);

    void setAutoPostForUser(User user, String extra);

    void unsetAutoPostForUser(User user);

    boolean hasBindedSteamAccount(User user);

}
