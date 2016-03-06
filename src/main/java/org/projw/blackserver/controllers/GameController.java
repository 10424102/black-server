package org.projw.blackserver.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.projw.blackserver.App;
import org.projw.blackserver.models.GameRepo;
import org.projw.blackserver.models.Game;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * Created by sk on 15-12-5.
 */
@RestController
public class GameController {

    @Autowired GameRepo gameRepo;
    @Autowired ApplicationContext context;
    @Autowired ObjectMapper objectMapper;

    /**
     * 获取活动的详细信息
     */
    @RequestMapping(value = App.API_GAME, method = GET)
    @Transactional(readOnly = false)
    public ResponseEntity<String> getGame(@RequestParam String key) throws JsonProcessingException{
        Game game = gameRepo.findOneByNameKey(key);
        if (game == null) return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
        game.setLocalizedName(context.getMessage("game." + game.getNameKey(), null,
                "", LocaleContextHolder.getLocale()));
        return ResponseEntity.ok(objectMapper.writeValueAsString(game));
    }
}
