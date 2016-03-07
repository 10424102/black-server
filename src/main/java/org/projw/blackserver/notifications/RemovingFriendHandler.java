package org.projw.blackserver.notifications;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.projw.blackserver.models.NotificationRepo;
import org.projw.blackserver.models.UserRepo;
import org.projw.blackserver.models.Notification;
import org.projw.blackserver.models.User;

@Component
@HandleNotificationType(Notification.FRIEND_REMOVE)
public class RemovingFriendHandler implements NotificationHandler {

    @Autowired NotificationRepo notificationRepo;

    @Autowired UserRepo userRepo;

    public void unfriend(User applicant,User target) {
        if (applicant == null || target == null) return;

        if (applicant.equals(target)) return;

        boolean removed = applicant.getFriendshipSet().removeIf(e -> e.getFriend().equals(target));

        if (!removed) return;

        target.getFriendshipSet().removeIf(e -> e.getFriend().equals(applicant));

        Notification notification = new Notification();
        notification.setTarget(target);
        notification.setType(Notification.FRIEND_REMOVE);
        notification.setDataId(applicant.getId());
        notificationRepo.save(notification); // 发出友尽通知
    }


    @Override
    public Notification inflateData(Notification notification) {
        Long id = notification.getDataId();
        User user = userRepo.findOne(id);
        notification.setData(user);
        return notification;
    }

    @Override
    public void handleReply(Notification notification, int reply) {
        // no-op
    }










}
