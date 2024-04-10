package com.github.devsns.domain.notifications.component;

import com.github.devsns.domain.notifications.entity.Notification;
import org.springframework.context.ApplicationEvent;
import org.springframework.stereotype.Component;

@Component
public class NotificationCreatedEvent extends ApplicationEvent {
    private final Notification notification;

    public NotificationCreatedEvent(Notification notification) {
        super(notification);
        this.notification = notification;
    }

    public Notification getNotification() {
        return notification;
    }
}