package org.team10424102.blackserver.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.security.web.method.annotation.AuthenticationPrincipalArgumentResolver;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.team10424102.blackserver.config.propertyeditors.UserResolver;

import java.util.List;

@Configuration
public class MvcConfig extends WebMvcConfigurerAdapter {

    @Autowired ApplicationContext context;

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
        //registry.addInterceptor(new LocalizationInterceptor()).addPathPatterns("/**");
    }

    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        /*ObjectMapper mapper = Jackson2ObjectMapperBuilder.json(
                .filters(new SimpleFilterProvider()).addFilter("filter", new Api.Result.ResultFilter());*/
        // Cannot use above code cause spring under 4.2

//        converters.replaceAll(f -> {
//            if (f instanceof MappingJackson2HttpMessageConverter) {
//                FilterProvider provider = new SimpleFilterProvider().addFilter(Api.RESULT_FILTER_NAME, new Api.Result.ResultFilter());
//                ((MappingJackson2HttpMessageConverter) f).getObjectMapper().setFilters(provider);
//            }
//            return f;
//        });
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(new AuthenticationPrincipalArgumentResolver()); // TODO kill it
        argumentResolvers.add(new UserResolver(context));
    }

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource source = new ReloadableResourceBundleMessageSource();
        source.setDefaultEncoding("UTF-8");
        source.setBasename("classpath:messages");
        return source;
    }
}
