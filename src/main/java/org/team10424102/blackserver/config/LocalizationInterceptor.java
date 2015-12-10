package org.team10424102.blackserver.config;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LocalizationInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        LocaleContextHolder.setLocale(request.getLocale());
        String lang = request.getHeader("Accept-Language");
        if (lang != null) {
            LocaleContextHolder.setLocale(StringUtils.parseLocaleString(lang));
        }
        return true; // go on
    }
}
