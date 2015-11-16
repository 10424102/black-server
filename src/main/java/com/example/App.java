package com.example;

import com.example.dev.DevHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.TimeZone;

@SpringBootApplication
@Component
public class App implements ApplicationContextAware {
    public static final String CFG_TOKEN_LIFETIME = "blackserver.token.lifetime";
    private static final Logger logger = LoggerFactory.getLogger(App.class);
    public static int tokenLifetime;

    public static void main(String[] args) {
        // 服务器使用 UTC 时间
        TimeZone.setDefault(TimeZone.getTimeZone("Etc/UTC"));
        // 初始化数据库
        DevHelper.initDb(args);
        // 启动服务器
        SpringApplication.run(App.class, args);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Environment env = applicationContext.getEnvironment();
        tokenLifetime = env.getProperty(CFG_TOKEN_LIFETIME, Integer.class, 180);
    }
}
