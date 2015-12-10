package org.team10424102.blackserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.team10424102.blackserver.config.ApplicationProperties;

import java.util.TimeZone;

@SpringBootApplication
@EnableConfigurationProperties(ApplicationProperties.class)
public class App {
    public static final String NAME = "blackserver";
    public static final String DEFAULT_AVATAR_TAG = "默认用户头像";
    public static final String DEFAULT_BACKGROUND_TAG = "默认用户背景";
    public static final String DEFAULT_COVER_TAG = "默认活动封面";
    public static final String API_USER = "/api/user";
    public static final String API_TOKEN = "/api/token";
    public static final String API_AVAILABILITY = "/api/availability";
    public static final String API_IMAGE = "/api/image";
    public static final String API_HEALTH = "/health";
    public static final String API_POST = "/api/post";
    public static final String API_ACTIVITY = "/api/activity";
    public static final String API_GAME = "/api/game";
    public static SpringApplication handle;

    public static void main(String[] args) {
        // 服务器使用 UTC 时间
        TimeZone.setDefault(TimeZone.getTimeZone("Etc/UTC"));
        // 启动服务器
        handle = new SpringApplication(App.class);
        //handle.addListeners(new ApplicationLifecycleManager());
        handle.run(args);
    }
}
