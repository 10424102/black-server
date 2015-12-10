package org.team10424102.blackserver.services;

import org.team10424102.blackserver.game.Game;

/**
 * Created by sk on 15-12-5.
 */
public interface GameService {

  Game getGame(String identifier);
}
