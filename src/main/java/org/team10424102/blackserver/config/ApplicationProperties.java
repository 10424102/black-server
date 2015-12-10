package org.team10424102.blackserver.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.team10424102.blackserver.App;

import javax.validation.constraints.NotNull;

@ConfigurationProperties(App.NAME)
public class ApplicationProperties {

    @NotNull
    private Chat chat;

    public Chat getChat() {
        return chat;
    }

    public void setChat(Chat chat) {
        this.chat = chat;
    }

    public static class Chat {
        /**
         * 聊天服务器域名
         */
        @NotNull
        private String hostname = "localhost";

        /**
         * 聊天服务器端口
         */
        @NotNull
        private int port = 8081;

        public String getHostname() {
            return hostname;
        }

        public void setHostname(String hostname) {
            this.hostname = hostname;
        }

        public int getPort() {
            return port;
        }

        public void setPort(int port) {
            this.port = port;
        }
    }
}
