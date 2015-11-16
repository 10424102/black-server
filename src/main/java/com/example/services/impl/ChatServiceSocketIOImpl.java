package com.example.services.impl;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import com.example.models.ChatMessage;
import com.example.services.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.annotation.PreDestroy;

@Service
public class ChatServiceSocketIOImpl implements ChatService, ConnectListener, DisconnectListener, DataListener<ChatMessage> {

    public static final String CFG_CHAT_HOST = "blackserver.chat.host";
    public static final String CFG_CHAT_PORT = "blackserver.chat.port";
    public static final String EVENT_CHAT = "chatevent";

    private final SocketIOServer server;

    @Autowired
    public ChatServiceSocketIOImpl(Environment env){
        String hostname = env.getProperty(CFG_CHAT_HOST);
        int port = env.getProperty(CFG_CHAT_PORT, Integer.class);
        Configuration cfg = new Configuration();
        cfg.setHostname(hostname);
        cfg.setPort(port);
        server = new SocketIOServer(cfg);
        server.addConnectListener(this);
        server.addDisconnectListener(this);
        server.addEventListener(EVENT_CHAT, ChatMessage.class, this);
    }

    @Override
    public void onConnect(SocketIOClient client) {
        sendSystemChatMessage("已连接");
    }

    @Override
    public void onData(SocketIOClient client, ChatMessage data, AckRequest ackSender) throws Exception {
        server.getBroadcastOperations().sendEvent(EVENT_CHAT, data);
    }

    @Override
    public void onDisconnect(SocketIOClient client) {
        sendSystemChatMessage("已断开连接");
    }

    private void sendSystemChatMessage(String message){
        server.getBroadcastOperations().sendEvent(EVENT_CHAT, new ChatMessage("聊天系统", message));
    }

    @PreDestroy
    public void shutdown() {
        server.stop();
    }
}
