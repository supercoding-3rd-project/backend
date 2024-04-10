package com.github.devsns.domain.notifications.component;


import com.github.devsns.domain.notifications.entity.*;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PostRemove;
import jakarta.persistence.PostUpdate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class NotificationEntityListener {
    private ApplicationEventPublisher eventPublisher;

    public NotificationEntityListener() {
        // 기본 생성자를 추가하여 no-arg constructor를 보장합니다.
    }

    @Autowired
    public void setEventPublisher(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }


    @PostPersist
    @PostUpdate
    @PostRemove
    public void postChange(Object entity) {
        if (entity instanceof Notification) {
            eventPublisher.publishEvent(new NotificationCreateEvent((Notification) entity));
        } else if (entity instanceof AnswerCommentNotification) {
            eventPublisher.publishEvent(new NotificationCreateEvent((Notification)entity));
        } else if (entity instanceof AnswerNotification) {
            eventPublisher.publishEvent(new NotificationCreateEvent((Notification)entity));
        } else if (entity instanceof FollowNotification) {
            eventPublisher.publishEvent(new NotificationCreateEvent((Notification)entity));
        } else if (entity instanceof LikeAnswerNotification) {
            eventPublisher.publishEvent(new NotificationCreateEvent((Notification)entity));
        } else if (entity instanceof LikeQuestionNotification) {
            eventPublisher.publishEvent(new NotificationCreateEvent((Notification)entity));
//        } else if (entity instanceof MessageNotification) {
//            log.info("MessageNotification entity changed. ID: {}", ((MessageNotification) entity).getId());
//            eventPublisher.publishEvent(new NotificationCreateEvent((Notification)entity));
        } else if (entity instanceof QuestionCommentNotification) {
            eventPublisher.publishEvent(new NotificationCreateEvent((Notification)entity));
        }
    }
}

