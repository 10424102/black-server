package org.team10424102.blackserver.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.team10424102.blackserver.config.propertyeditors.UserResolver;
import org.team10424102.blackserver.daos.AcademyRepo;
import org.team10424102.blackserver.daos.CollegeRepo;
import org.team10424102.blackserver.services.ImageService;

import java.util.List;

@Configuration
public class MvcConfig extends WebMvcConfigurerAdapter {

    @Autowired CollegeRepo collegeRepo;
    @Autowired AcademyRepo academyRepo;
    @Autowired ImageService imageService;

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("index");
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
        argumentResolvers.add(new UserResolver(collegeRepo, academyRepo, imageService));
    }

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource source = new ReloadableResourceBundleMessageSource();
        source.setDefaultEncoding("UTF-8");
        source.setBasename("classpath:messages");
        return source;
    }
}
