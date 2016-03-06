package org.projw.blackserver.notifications;

import org.projw.blackserver.models.Notification;

public interface NotificationHandler {
    Notification inflateData(Notification notification);
    void handleReply(Notification notification, int reply);
}
