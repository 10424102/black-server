package org.team10424102.blackserver.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.team10424102.blackserver.App;
import org.team10424102.blackserver.config.json.Views;
import org.team10424102.blackserver.game.Game;
import org.team10424102.blackserver.services.GameService;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * Created by sk on 15-12-5.
 */
@RestController
public class GameController {

    @Autowired GameService gameService;

    @Autowired ApplicationContext context;

    /**
     * 获取活动的详细信息
     */
    @RequestMapping(value = App.API_GAME, method = GET)
    @JsonView(Views.Game.class)
    public Game getGame(@RequestParam String identifier) {
        Game game = gameService.getGame(identifier);
        game.setLocalizedName(context.getMessage("game." + game.getIdentifier(), null,
                "", LocaleContextHolder.getLocale()));
        return game;
    }
}
