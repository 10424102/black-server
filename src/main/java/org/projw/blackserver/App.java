package org.projw.blackserver;

import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.DispatcherType;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import java.util.EnumSet;

public class App implements WebApplicationInitializer {
    public static final String DEFAULT_AVATAR_TAG = "默认用户头像";
    public static final String DEFAULT_BACKGROUND_TAG = "默认用户背景";
    public static final String DEFAULT_COVER_TAG = "默认活动封面";

    public static final String API_USER = "/api/users";
    public static final String API_IMAGE = "/api/images";
    public static final String API_POST = "/api/posts";
    public static final String API_ACTIVITY = "/api/activities";
    public static final String API_GAME = "/api/games";
    public static final String API_NOTIFICATION = "/api/notifications";


    @Override
    public void onStartup(ServletContext container) throws ServletException {

        // ContextLoaderListener
        // DispatcherServlet
        // DelegatingFilterProxy springSecurityFilterChain

        WebApplicationContext context = getContext();
        container.addListener(new ContextLoaderListener(context));

        ServletRegistration.Dynamic dispatcher = container.addServlet("dispatcherServlet", new DispatcherServlet(context));
        dispatcher.setLoadOnStartup(1);
        dispatcher.addMapping("/");

        String filterName = AbstractSecurityWebApplicationInitializer.DEFAULT_FILTER_NAME;
        container.addFilter(filterName, DelegatingFilterProxy.class)
                .addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), false, "/*");
    }

    private AnnotationConfigWebApplicationContext getContext() {
        AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
        context.setConfigLocation("org.projw.config.RootConfig");
        return context;
    }

//    public static void main(String[] args) throws Exception {
//        // 服务器使用 UTC 时间
//        TimeZone.setDefault(TimeZone.getTimeZone("Etc/UTC"));
//
////        AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
////        context.register(AppConfig.class);
////        context.getEnvironment().setDefaultProfiles("dev");
////
////        Server server = new Server(8080); // bind on 0.0.0.0
////
////        ServletContextHandler handler = new ServletContextHandler();
////        handler.setContextPath("/");
////        handler.setResourceBase(new ClassPathResource("static").getURI().toString());
////        handler.addServlet(new ServletHolder(new DispatcherServlet(context)), "/*");
////        handler.addEventListener(new ContextLoaderListener(context));
////        handler.addFilter(new FilterHolder(new DelegatingFilterProxy("springSecurityFilterChain")),
////                "/*", EnumSet.allOf(DispatcherType.class));
////        server.setHandler(handler);
////
////        server.start();
////        server.join();
//    }
}
