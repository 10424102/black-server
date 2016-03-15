package org.projw.blackserver.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import org.hibernate.SessionFactory;
import org.projw.blackserver.config.json.ImageSerializer;
import org.projw.blackserver.config.propertyeditors.UserResolver;
import org.projw.blackserver.models.Image;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.orm.hibernate4.support.OpenSessionInViewInterceptor;
import org.springframework.security.web.method.annotation.AuthenticationPrincipalArgumentResolver;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.*;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;
import org.thymeleaf.templateresolver.TemplateResolver;

import javax.persistence.EntityManagerFactory;
import java.nio.charset.Charset;
import java.util.List;

@Configuration
@EnableWebMvc
@EnableSpringDataWebSupport
public class WebConfig extends WebMvcConfigurerAdapter {

    @Autowired
    EntityManagerFactory entityManagerFactory;

    @Autowired
    ApplicationContext context;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**").addResourceLocations("/");
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("index");
        registry.addViewController("/partials/users").setViewName("users");
        registry.addViewController("/partials/activities").setViewName("activities");
        registry.addViewController("/partials/login").setViewName("login");
        registry.addViewController("/partials/chatroom").setViewName("chatroom");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new DebugInterceptor()).addPathPatterns("/**");
        registry.addInterceptor(new LocalizationInterceptor()).addPathPatterns("/**");

        OpenSessionInViewInterceptor interceptor = new OpenSessionInViewInterceptor();
        interceptor.setSessionFactory(entityManagerFactory.unwrap(SessionFactory.class));
        registry.addWebRequestInterceptor(interceptor).addPathPatterns("/**");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(new AuthenticationPrincipalArgumentResolver()); // TODO kill it ?
        argumentResolvers.add(new UserResolver(context));
    }

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        Hibernate5Module module = new Hibernate5Module(entityManagerFactory.unwrap(SessionFactory.class));
        module.enable(Hibernate5Module.Feature.FORCE_LAZY_LOADING);
        module.enable(Hibernate5Module.Feature.REQUIRE_EXPLICIT_LAZY_LOADING_MARKER);
        objectMapper.registerModule(module);

        SimpleModule sm = new SimpleModule();
        sm.addSerializer(Image.class, new ImageSerializer(context));
        objectMapper.registerModule(sm);


        return objectMapper;
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        //converters.add(new MappingJackson2HttpMessageConverter(objectMapper()));
        converters.add(new ByteArrayHttpMessageConverter());
        converters.add(new StringHttpMessageConverter(Charset.forName("UTF-8")));
    }

    @Bean
    public TemplateResolver templateResolver() {
        ServletContextTemplateResolver resolver = new ServletContextTemplateResolver();
        resolver.setPrefix("/templates/");
        resolver.setSuffix(".html");
        resolver.setTemplateMode("LEGACYHTML5");
        return resolver;
    }

    @Bean
    public TemplateEngine templateEngine() {
        SpringTemplateEngine engine = new SpringTemplateEngine();
        engine.setTemplateResolver(templateResolver());
        return engine;
    }


}
