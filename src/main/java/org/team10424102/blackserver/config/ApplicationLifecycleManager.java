package org.team10424102.blackserver.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class ApplicationLifecycleManager{
    private static final Logger logger = LoggerFactory.getLogger(ApplicationLifecycleManager.class);

    @EventListener
    public void handleContextRefresh(ContextRefreshedEvent event) {
        logger.info(">>> 服务器成功启动，撒花 (￣▽￣)o∠※PAN!=.:*:'☆.:*:'★':*");
    }
}
