package com.example.dev;

import com.example.daos.UserRepo;
import com.example.models.User;
import com.example.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class DebugController {
    @Autowired
    DebugManager debugManager;

    @Autowired
    UserRepo userRepo;

    @Autowired
    TokenService tokenService;

    @RequestMapping("/requests")
    public String getAllRequests(Model model) {
        model.addAttribute("requests", debugManager.requests);
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
