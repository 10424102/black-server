package com.example.config;

import com.example.Api;
import com.example.config.converters.json.BlackServerDataJacksonModule;
import com.example.dev.DebugRequestInterceptor;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.List;

@Configuration
public class MvcConfig extends WebMvcConfigurerAdapter {

    @Autowired TokenAuthenticationInterceptor tokenAuthenticationInterceptor;

    @Autowired DebugRequestInterceptor debugRequestInterceptor;

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("index");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(debugRequestInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/requests", "/tokens", "/", "/error");
        registry.addInterceptor(tokenAuthenticationInterceptor)
                .addPathPatterns("/api/**")
                .excludePathPatterns("/api/register/**", "/api/token/**", "/api/availability/**");
    }

    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        /*ObjectMapper mapper = Jackson2ObjectMapperBuilder.json()
                .filters(new SimpleFilterProvider()).addFilter("filter", new Api.Result.ResultFilter());*/
        // Cannot use above code cause spring under 4.2

        converters.replaceAll(f -> {
            if (f instanceof MappingJackson2HttpMessageConverter) {
                FilterProvider provider = new SimpleFilterProvider().addFilter(Api.RESULT_FILTER_NAME, new Api.Result.ResultFilter());
                ((MappingJackson2HttpMessageConverter) f).getObjectMapper().setFilters(provider);
            }
            return f;
        });
    }

    @Bean
    Module module(){
        BlackServerDataJacksonModule blackServerDataJacksonModule = new BlackServerDataJacksonModule();
        return blackServerDataJacksonModule;
    }
}
