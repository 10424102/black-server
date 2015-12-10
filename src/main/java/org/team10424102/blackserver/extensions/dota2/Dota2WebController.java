package org.team10424102.blackserver.extensions.dota2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.team10424102.blackserver.models.User;
import org.team10424102.blackserver.config.security.CurrentUser;

import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
@RequestMapping("/api/dota2")
public class Dota2WebController {

    @Autowired Dota2MatchResultAutoPostService postService;

    /**
     * 用户设置 Dota2 战绩自动转发功能
     * @param value on|off
     * @param extra 自动发送战绩时候的个性化标语
     * @param user 自动注入 SpringSecurity 的当前用户
     */
    @RequestMapping(value = "/autopost/{value}", method = PUT)
    public void autoPostSwitch(@PathVariable String value, @RequestParam String extra, @CurrentUser User user) {
        // 用户未绑定 Steam 账号忽略请求
        if (!postService.hasBindedSteamAccount(user)) return;

        switch (value) {
            case "on":
                postService.setAutoPostForUser(user, extra);
                break;
            case "off":
                postService.unsetAutoPostForUser(user);
                break;
        }
    }

    @RequestMapping(value = "/account", method = POST)
    public void bindingSteamAccount(@CurrentUser User user) {
        // TODO not implemented, use oauth maybe?
    }

    @RequestMapping(value = "/account", method = DELETE)
    public void unbindingStreamAccount(@CurrentUser User user) {

    }
}
