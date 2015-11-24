package com.example.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.List;

@Configuration
public class MvcConfig extends WebMvcConfigurerAdapter {

//    @Autowired TokenAuthenticationInterceptor tokenAuthenticationInterceptor;
//
//    @Autowired DebugRequestInterceptor debugRequestInterceptor;

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("index");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(debugRequestInterceptor)
//                .addPathPatterns("/**")
//                .excludePathPatterns("/requests", "/tokens", "/", "/error");
//        registry.addInterceptor(tokenAuthenticationInterceptor)
//                .addPathPatterns("/api/**")
//                .excludePathPatterns("/api/register/**", "/api/token/**", "/api/availability/**");
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

//    @Bean
//    public Hibernate4Module hibernate4Module(){
//        return new Hibernate4Module();
//    }

//    @Bean
//    public Jackson2ObjectMapperBuilder jackson2ObjectMapperBuilder(){
//        Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();
//        ObjectMapper mapper = new ObjectMapper();
//        SimpleModule module = new SimpleModule();
//        module.addSerializer(Image.class, new ImageSerializer());
//        mapper.registerModule(module);
//        builder.configure(mapper);
//        return builder;
//    }

//    @Bean
//    public CustomEditorConfigurer customEditorConfigurer(){
//        CustomEditorConfigurer configurer = new CustomEditorConfigurer();
//        CustomPropertyEditorRegistrar registrar = new CustomPropertyEditorRegistrar();
//        configurer.setPropertyEditorRegistrars(new PropertyEditorRegistrar[]{registrar});
//        return configurer;
//    }


//    @Override
//    public void addFormatters(FormatterRegistry registry) {
//        registry.addConverter(new StringToUserConverter());
//    }
}
