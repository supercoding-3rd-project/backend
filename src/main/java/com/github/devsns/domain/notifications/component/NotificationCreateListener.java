package com.github.devsns.domain.notifications.component;

import com.github.devsns.domain.notifications.config.NotificationWebSocketHandler;
import com.github.devsns.domain.notifications.entity.Notification;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class NotificationCreateListener {
    private final NotificationWebSocketHandler notificationWebSocketHandler;


    @TransactionalEventListener
    public void onNotificationCreate(NotificationCreateEvent event) {
        Notification notification = event.getNotification();
        notificationWebSocketHandler.handleNotificationCreated(notification);
    }
}

