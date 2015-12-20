package org.team10424102.blackserver.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import org.hibernate.SessionFactory;
import org.hibernate.dialect.function.TemplateRenderer;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.http.HttpMethod;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate4.support.OpenSessionInViewInterceptor;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.method.annotation.AuthenticationPrincipalArgumentResolver;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.*;
import org.team10424102.blackserver.App;
import org.team10424102.blackserver.config.json.ImageSerializer;
import org.team10424102.blackserver.config.propertyeditors.UserResolver;
import org.team10424102.blackserver.models.Image;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;
import org.thymeleaf.templateresolver.TemplateResolver;

import javax.persistence.EntityManagerFactory;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.util.List;
import java.util.Properties;

@Configuration
@ComponentScan(basePackages = "org.team10424102.blackserver")
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "org.team10424102.blackserver")
@EnableScheduling
public class AppConfig {

    @Configuration
    @EnableWebMvc
    @EnableSpringDataWebSupport
    protected static class WebMvcConfig extends WebMvcConfigurerAdapter {

        @Autowired EntityManagerFactory entityManagerFactory;

        @Autowired ApplicationContext context;

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
            argumentResolvers.add(new AuthenticationPrincipalArgumentResolver()); // TODO kill it
            argumentResolvers.add(new UserResolver(context));
        }

        @Bean
        public ObjectMapper objectMapper() {
            ObjectMapper objectMapper = new ObjectMapper();
            Hibernate5Module module = new Hibernate5Module();
            //module.enable(Hibernate5Module.Feature.FORCE_LAZY_LOADING);
            objectMapper.registerModule(module);

            SimpleModule sm = new SimpleModule();
            sm.addSerializer(Image.class, new ImageSerializer(context));
            objectMapper.registerModule(sm);


            return objectMapper;
        }

        @Override
        public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
            converters.add(new MappingJackson2HttpMessageConverter(objectMapper()));
        }

        @Bean
        public TemplateResolver templateResolver() {
            ServletContextTemplateResolver resolver = new ServletContextTemplateResolver();
            resolver.setPrefix("/templates/");
            resolver.setSuffix(".html");
            resolver.setTemplateMode("HTML5");
            return resolver;
        }

        @Bean
        public TemplateEngine templateEngine() {
            SpringTemplateEngine engine = new SpringTemplateEngine();
            engine.setTemplateResolver(templateResolver());
            return engine;
        }
    }

    @Configuration
    @EnableWebSecurity // will add bean springSecurityFilterChain
    @EnableGlobalMethodSecurity(prePostEnabled = true)
    protected static class SecurityConfig extends WebSecurityConfigurerAdapter {
        @Autowired ApplicationContext context;

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.addFilterBefore(new TokenAuthenticationFilter(context), BasicAuthenticationFilter.class)
                    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    .and()
                    .csrf().disable()
                    .httpBasic().disable()
                    .authorizeRequests()
                    .antMatchers("/", "/partials/**").permitAll()
                    .antMatchers(HttpMethod.GET, App.API_USER + "/token").permitAll()
                    .antMatchers(HttpMethod.HEAD, App.API_USER + "/token").permitAll()
                    .antMatchers(HttpMethod.HEAD, App.API_USER + "/phone").permitAll()
                    .antMatchers("/beans").permitAll()
                    .antMatchers("/health").permitAll()
                    .anyRequest().authenticated()
                    .and().exceptionHandling().authenticationEntryPoint(unauthorizedEntryPoint());
        }

        @Bean
        public AuthenticationEntryPoint unauthorizedEntryPoint() {
            return (request, response, authException) -> response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        }

        @Configuration
        @Order(Ordered.HIGHEST_PRECEDENCE)
        protected static class CleanAuthenticationManager extends GlobalAuthenticationConfigurerAdapter {

            @Override
            public void init(AuthenticationManagerBuilder auth) throws Exception {
                // do nothing
                auth.authenticationProvider(null);
            }
        }
    }

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource source = new ReloadableResourceBundleMessageSource();
        source.setDefaultEncoding("UTF-8");
        source.setBasename("classpath:messages");
        return source;
    }

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost:3306/black_server?userUnicode=true&characterEncoding=UTF-8");
        dataSource.setUsername("bsadmin");
        dataSource.setPassword("bspass");
        return dataSource;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactoryBean.setDataSource(dataSource());
        entityManagerFactoryBean.setPackagesToScan("org.team10424102.blackserver");
        entityManagerFactoryBean.setPersistenceProviderClass(HibernatePersistenceProvider.class);
        entityManagerFactoryBean.setJpaProperties(hibernateProperties());
        return entityManagerFactoryBean;
    }

    private Properties hibernateProperties() {
        Properties properties = new Properties();
        properties.put("hibernate.dialect", "org.hibernate.dialect.MySQL5InnoDBDialect");
        properties.put("hibernate.show_sql", "true");
        properties.put("hibernate.ddl-auto", "validate");
        properties.put("hibernate.physical_naming_strategy", "org.team10424102.blackserver.config.ImprovedNamingStrategy");
        properties.put("hibernate.format_sql", "true");
        properties.put("hibernate.id.new_generator_mappings", "false");
        return properties;
    }

    @Bean
    public JpaTransactionManager transactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory().getObject());
        return transactionManager;
    }
}
