package org.projw.blackserver.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.projw.blackserver.models.UserRepo;
import org.projw.blackserver.models.RegInfo;
import org.projw.blackserver.models.User;
import org.projw.blackserver.services.TokenService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Controller
public class DebugController {

    @Autowired UserRepo userRepo;

    @Autowired TokenService tokenService;

    @RequestMapping("/requests")
    public String getAllRequests(Model model) {
        return "requests";
    }

    @RequestMapping("/tokens")
    public String allTokens(Model model) {
        Iterable<User> users = userRepo.findAll();
        List<UserWithToken> tokens = new ArrayList<>();
        for (User user : users) {
            UserWithToken u = new UserWithToken();
            u.setPhone(user.getPhone());
            u.setToken(tokenService.generateToken(user));
            tokens.add(u);
        }
        model.addAttribute("tokens", tokens);
        return "tokens";
    }

    @RequestMapping(value = "/api/reginfo", method = GET)
    public ResponseEntity<?> getMyRegInfo() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (user != null) {
            RegInfo regInfo = user.getRegInfo();
            return new ResponseEntity<>(regInfo, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    public static class Request {
        private String host;
        private String method;
        private String type;
        private int contentLength;
        private String url;
        private Map<String, List<String>> headers = new HashMap<>();
        private String content;

        public String getHost() {
            return host;
        }

        public void setHost(String host) {
            this.host = host;
        }

        public String getMethod() {
            return method;
        }

        public void setMethod(String method) {
            this.method = method;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public int getContentLength() {
            return contentLength;
        }

        public void setContentLength(int contentLength) {
            this.contentLength = contentLength;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public Map<String, List<String>> getHeaders() {
            return headers;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }

    public static class UserWithToken {
        private String phone;
        private String token;

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }
    }
}
