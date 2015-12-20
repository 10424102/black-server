package org.team10424102.blackserver;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.DispatcherServlet;
import org.team10424102.blackserver.config.AppConfig;

import javax.servlet.DispatcherType;
import java.util.EnumSet;
import java.util.TimeZone;

public class App {
    public static final String DEFAULT_AVATAR_TAG = "默认用户头像";
    public static final String DEFAULT_BACKGROUND_TAG = "默认用户背景";
    public static final String DEFAULT_COVER_TAG = "默认活动封面";

    public static final String API_USER = "/api/users";
    public static final String API_IMAGE = "/api/images";
    public static final String API_POST = "/api/posts";
    public static final String API_ACTIVITY = "/api/activities";
    public static final String API_GAME = "/api/games";
    public static final String API_NOTIFICATION = "/api/notifications";


    public static void main(String[] args) throws Exception {
        // 服务器使用 UTC 时间
        TimeZone.setDefault(TimeZone.getTimeZone("Etc/UTC"));

        AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
        context.register(AppConfig.class);
        context.getEnvironment().setDefaultProfiles("dev");

        Server server = new Server(8080);
        ServletContextHandler handler = new ServletContextHandler();
        handler.setContextPath("/");
        handler.setResourceBase(new ClassPathResource("static").getURI().toString());
        handler.addServlet(new ServletHolder(new DispatcherServlet(context)), "/*");
        handler.addEventListener(new ContextLoaderListener(context));
        handler.addFilter(new FilterHolder(new DelegatingFilterProxy("springSecurityFilterChain")),
                "/*", EnumSet.allOf(DispatcherType.class));
        server.setHandler(handler);

        server.start();
        server.join();
    }
}
