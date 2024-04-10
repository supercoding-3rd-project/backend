package com.github.devsns.domain.notifications.component;


import com.github.devsns.domain.notifications.entity.Notification;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PostRemove;
import jakarta.persistence.PostUpdate;
import jakarta.persistence.PrePersist;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Component;

@Component
public class NotificationListener implements ApplicationEventPublisherAware {
    private ApplicationEventPublisher eventPublisher;

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.eventPublisher = applicationEventPublisher;
    }
    @PostPersist
    @PostUpdate
    @PostRemove
    public void handleNotification(Notification notification) {
        // Notification 엔티티 저장 후, 커스텀 이벤트 발행
        eventPublisher.publishEvent(new NotificationCreatedEvent(notification));
    }
}
