package org.team10424102.blackserver.config;

import org.springframework.context.ApplicationContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.filter.OncePerRequestFilter;
import org.team10424102.blackserver.services.TokenService;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
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
    private final TokenService tokenService;
    private final EntityManager entityManager;

    public TokenAuthenticationFilter(ApplicationContext context) {
        tokenService  = context.getBean(TokenService.class);
        entityManager = context.getBean(EntityManager.class);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        String token = request.getHeader(HTTP_HEADER);
        if (token == null) {
            chain.doFilter(request, response);
            return;
        }
        SpringSecurityUserAdapter auth = (SpringSecurityUserAdapter)tokenService.getObjectFromToken(token);
        if (auth != null) {
            SecurityContextHolder.getContext().setAuthentication(auth);
        } else {
            SecurityContextHolder.clearContext();
        }
        chain.doFilter(request, response);
    }
}
