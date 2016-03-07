package org.projw.blackserver.notifications;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.projw.blackserver.models.FriendshipApplicationRepo;
import org.projw.blackserver.models.NotificationRepo;
import org.projw.blackserver.models.UserRepo;
import org.projw.blackserver.models.Friendship;
import org.projw.blackserver.models.FriendshipApplication;
import org.projw.blackserver.models.Notification;
import org.projw.blackserver.models.User;

import java.util.Calendar;

import static org.projw.blackserver.models.Notification.*;

@Component
@HandleNotificationType({FRIEND_ADD, FRIEND_ADD_PENDING, FRIEND_ADD_REPLY})
public class AddingFriendHandler implements NotificationHandler {

    @Autowired FriendshipApplicationRepo friendshipApplicationRepo;

    @Autowired NotificationRepo notificationRepo;

    @Autowired UserRepo userRepo;

    public void fireRequest(User applicant, User target, String attachment) {
        if (applicant == null || target == null) return;

        // 不能和自己成为朋友
        if (applicant.equals(target)) return;

        // 你在对方的黑名单中
        if (target.getBlacklist().contains(applicant)) return;

        FriendshipApplication application = new FriendshipApplication();
        application.setApplicant(applicant);
        application.setAttachment(attachment);
        application.setCreationTime(Calendar.getInstance().getTime());
        application.setTarget(target);
        application = friendshipApplicationRepo.save(application);

        Notification notification = new Notification();
        notification.setTarget(target);
        notification.setType(Notification.FRIEND_ADD);
        notification.setDataId(application.getId());
        notificationRepo.save(notification); // 发出好友申请

        notification = new Notification();
        notification.setTarget(applicant);
        notification.setType(Notification.FRIEND_ADD_PENDING);
        notification.setDataId(application.getId());
        notificationRepo.save(notification); // 同时自己也会获得正在等待对方确认的通知

        application.setApplicantNotification(notification);
        friendshipApplicationRepo.save(application);
    }


    @Override
    public Notification inflateData(Notification notification) {
        Long id = notification.getDataId();
        FriendshipApplication application = friendshipApplicationRepo.findOne(id);
        notification.setData(application);
        return notification;
    }

    @Override
    public void handleReply(Notification notification, int reply) {
        if (notification == null) return;
        switch (notification.getType()) {
            case FRIEND_ADD:
                if (reply == REPLY_YES) {
                    inflateData(notification);
                    FriendshipApplication application = (FriendshipApplication) notification.getData();

                    User applicant = application.getApplicant();
                    User target = application.getTarget();

                    Friendship friendship = new Friendship();
                    friendship.setUser(applicant);
                    friendship.setFriend(target);
                    applicant.getFriendshipSet().add(friendship);
                    userRepo.save(applicant);

                    friendship = new Friendship();
                    friendship.setUser(target);
                    friendship.setFriend(applicant);
                    target.getFriendshipSet().add(friendship);
                    userRepo.save(target);

                    Notification applicantNotification = application.getApplicantNotification();
                    notificationRepo.delete(applicantNotification);
                    notificationRepo.delete(notification);

                    Notification n = new Notification();
                    n.setTarget(applicant);
                    n.setType(FRIEND_ADD_REPLY);
                    n.setDataId(application.getId());
                    n.setReply(REPLY_YES);
                    notificationRepo.save(n); // 回复

                } else if (reply == REPLY_NO) {
                    inflateData(notification);
                    FriendshipApplication application = (FriendshipApplication) notification.getData();

                    Notification applicantNotification = application.getApplicantNotification();
                    notificationRepo.delete(applicantNotification);
                    notificationRepo.delete(notification);

                    User applicant = application.getApplicant();

                    Notification n = new Notification();
                    n.setTarget(applicant);
                    n.setType(FRIEND_ADD_REPLY);
                    n.setDataId(application.getId());
                    n.setReply(REPLY_NO);
                    notificationRepo.save(n); // 回复

                } else if (reply == REPLY_DELETE) {
                    notificationRepo.delete(notification);
                }
                break;
            case FRIEND_ADD_PENDING:
                break;
            case FRIEND_ADD_REPLY:
                if (reply == REPLY_DELETE) {
                    long dataId = notification.getDataId();
                    friendshipApplicationRepo.delete(dataId);
                    notificationRepo.delete(notification);
                }
                break;
        }
    }


}
