package org.team10424102.blackserver.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.team10424102.blackserver.App;
import org.team10424102.blackserver.config.json.Views;
import org.team10424102.blackserver.config.CurrentUser;
import org.team10424102.blackserver.models.NotificationRepo;
import org.team10424102.blackserver.models.Notification;
import org.team10424102.blackserver.models.User;
import org.team10424102.blackserver.notifications.AddingFriendHandler;
import org.team10424102.blackserver.notifications.HandleNotificationType;
import org.team10424102.blackserver.notifications.NotificationHandler;
import org.team10424102.blackserver.notifications.RemovingFriendHandler;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping(App.API_NOTIFICATION)
public class NotificationController {

    @Autowired NotificationRepo notificationRepo;

    @Autowired ApplicationContext context;

    private Map<Integer, NotificationHandler> handlers = new ConcurrentHashMap<>();
    private static final Class[] classes = new Class[]{
            AddingFriendHandler.class,
            RemovingFriendHandler.class
    };

    @PostConstruct
    public void init() {
        for (Class cls : classes) {
            HandleNotificationType type =
                    (HandleNotificationType) cls.getAnnotation(HandleNotificationType.class);
            int[] values = type.value();
            for (int value : values) {
                handlers.put(value, (NotificationHandler) context.getBean(cls));
            }
        }
    }

    @RequestMapping(method = RequestMethod.GET)
    @JsonView(Views.Notification.class)
    @Transactional
    public List<Notification> getNotifications(@CurrentUser User user) {
        Iterator<Notification> i = notificationRepo.findAllByTarget(user).iterator();
        return StreamSupport
                .stream(Spliterators.spliteratorUnknownSize(i, Spliterator.ORDERED), false)
                .map(e -> {
                    NotificationHandler handler = handlers.get(e.getType());
                    if (handler != null) return handler.inflateData(e);
                    return e;
                })
                .collect(Collectors.toList());
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    @Transactional
    public void handleReply(@PathVariable long id, @RequestParam int reply) {
        Notification notification = notificationRepo.findOne(id);
        handlers.get(notification.getType()).handleReply(notification, reply);
    }



}
