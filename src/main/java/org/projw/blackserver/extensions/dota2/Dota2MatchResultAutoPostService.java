package org.projw.blackserver.extensions.dota2;

import org.projw.blackserver.models.User;

public interface Dota2MatchResultAutoPostService {

    void createSteamAccount(Dota2SteamAccount account);

    void deleteSteamAccount(Dota2SteamAccount account);

    void setAutoPostForUser(User user, String extra);

    void unsetAutoPostForUser(User user);

    boolean hasBindedSteamAccount(User user);

}
