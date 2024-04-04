package com.github.devsns.domain.notifications.component;

import com.github.devsns.domain.notifications.entity.Notification;
import org.springframework.context.ApplicationEvent;

public class NotificationCreatedEvent extends ApplicationEvent {
    private Notification notification;

    public NotificationCreatedEvent(Object source, Notification notification) {
        super(source);
        this.notification = notification;
    }

    public Notification getNotification() {
        return notification;
    }
}