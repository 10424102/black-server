package com.example.config;

import com.example.exceptions.IllegalTokenException;
import com.example.services.TokenService;
import com.example.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class TokenAuthenticationInterceptor implements HandlerInterceptor {
    static final Logger log = LoggerFactory.getLogger(TokenAuthenticationInterceptor.class);

    @Autowired TokenService tokenService;

    @Autowired UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String token = request.getHeader("X-Token");
        if (token == null) {
            log.debug("拦截到无 X-Token 的请求：" + request.getRequestURI());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        } else {
            log.debug("拦截到 X-Token 为 " + token + " 的请求：" + request.getRequestURI());
            try {

                userService.addUser(tokenService.getUserId(token));
            }catch (IllegalTokenException e){
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return false;
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
