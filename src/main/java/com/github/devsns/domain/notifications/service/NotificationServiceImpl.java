package com.github.devsns.domain.notifications.service;

import com.github.devsns.domain.notifications.dto.NotificationDto;
import com.github.devsns.domain.notifications.entity.Notification;
import com.github.devsns.domain.notifications.repository.NotificationRepository;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final SimpMessagingTemplate messagingTemplate;

    public NotificationServiceImpl(NotificationRepository notificationRepository, SimpMessagingTemplate messagingTemplate) {
        this.notificationRepository = notificationRepository;
        this.messagingTemplate = messagingTemplate;
    }

    public void createNotification(NotificationDto notificationDto) {
        Notification notification = new Notification();

        notification.setMessage(notificationDto.getMessage());
        notification.setLikeNotification(notificationDto.getLikeNotification());
        notification.setCommentNotification(notificationDto.getCommentNotification());
        notification.setFollowNotification(notificationDto.getFollowNotification());
        notification.setMessageNotification(notificationDto.getMessageNotification());
        notification.setLikeNotificationCount(notificationDto.getLikeNotificationCount());
        notification.setCommentNotificationCount(notificationDto.getCommentNotificationCount());
        notification.setFollowNotificationCount(notificationDto.getFollowNotificationCount());
        notification.setMessageNotificationCount(notificationDto.getMessageNotificationCount());
        notificationRepository.save(notification);

        messagingTemplate.convertAndSend("/notifications", notificationDto);
    }
}
