package org.projw.blackserver.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;

public class DebugInterceptor extends HandlerInterceptorAdapter {
    private static final Logger logger = LoggerFactory.getLogger(DebugInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Enumeration<String> names = request.getParameterNames();
        StringBuilder builder = new StringBuilder("[");
        while (names.hasMoreElements()) {
            String name = names.nextElement();
            builder.append(name).append(": ").append(request.getParameter(name)).append(", ");
        }
        if (builder.length()>2) {
            builder.delete(builder.length() - 2, builder.length());
        }
        builder.append("]");
        logger.debug(String.format("%-6s %s %s", request.getMethod(), request.getRequestURI(), builder.toString()));


        return true;
    }
}
