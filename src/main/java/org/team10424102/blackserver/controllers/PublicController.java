package org.team10424102.blackserver.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.team10424102.blackserver.models.BlackServer;

@RestController
public class PublicController {

    @Autowired ObjectMapper objectMapper;

    /**
     * 获取服务器状态, 目前只会返回 OK
     */
    @RequestMapping("/status")
    public String getServerStatus() throws JsonProcessingException{
        return objectMapper.writeValueAsString(BlackServer.getInstance());
    }




}
