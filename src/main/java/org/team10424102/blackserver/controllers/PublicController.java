package org.team10424102.blackserver.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PublicController {

    /**
     * 获取服务器状态, 目前只会返回 OK
     */
    @RequestMapping("/status")
    public String getServerStatus() {
        return "OK";
    }




}
