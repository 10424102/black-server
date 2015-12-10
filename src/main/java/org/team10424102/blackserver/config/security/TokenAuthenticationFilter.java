package org.team10424102.blackserver.config.security;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import org.team10424102.blackserver.services.UserService;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 自定义的 Token 验证过滤器
 */
public class TokenAuthenticationFilter extends OncePerRequestFilter {
    public static final String HTTP_HEADER = "X-Token";
    private final UserService userService;

    public TokenAuthenticationFilter(UserService userService) {
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        String token = request.getHeader(HTTP_HEADER);
        if (token == null) {
            chain.doFilter(request, response);
            return;
        }
        UserAuthentication auth = userService.getUserAuthenticationFromToken(token);
        if (auth != null) {
            SecurityContextHolder.getContext().setAuthentication(auth);
        } else {
            SecurityContextHolder.clearContext();
        }
        chain.doFilter(request, response);
    }
}
