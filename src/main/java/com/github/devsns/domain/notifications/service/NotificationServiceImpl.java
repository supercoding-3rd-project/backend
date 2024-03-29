package com.github.devsns.domain.notifications.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.devsns.domain.notifications.constant.NotificationType;
import com.github.devsns.domain.notifications.dto.NotificationDto;
import com.github.devsns.domain.notifications.entity.Notification;
import com.github.devsns.domain.notifications.repository.NotificationRepository;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationServiceImpl implements NotificationService {
    private final NotificationRepository notificationRepository;
    private final SimpMessagingTemplate messagingTemplate;

    public NotificationServiceImpl(NotificationRepository notificationRepository, SimpMessagingTemplate messagingTemplate) {
        this.notificationRepository = notificationRepository;
        this.messagingTemplate = messagingTemplate;
    }

    public void sendRecentNotificationsToUser(Long userId, WebSocketSession session) {
        // 사용자에게 전송할 최근 알림을 조회
        List<Notification> recentNotifications = notificationRepository.findTop10ByUserIdOrderByCreatedAtDesc(userId);

        // 조회된 알림을 WebSocket을 통해 사용자에게 전송
        ObjectMapper mapper = new ObjectMapper();
        try {
            String jsonData = mapper.writeValueAsString(recentNotifications);
            session.sendMessage(new TextMessage(jsonData));
        } catch (IOException e) {
            throw new RuntimeException("Failed to send recent notifications to user", e);
        }
    }


    public void sendCommentNotification(User recipient, User actor, Post post, Comment comment) {
        Notification notification = new Notification();
        notification.setRecipient(recipient);
        notification.setActor(actor);
        notification.setPost(post);
        notification.setComment(comment);
        notification.setMessage(actor.getUsername() + "님이 댓글을 달았습니다.");
        notification.setType(NotificationType.COMMENT);
        notificationRepository.save(notification);
    }

    public void sendCommentLikeNotification(User recipient, User actor, Post post, Comment comment) {
        Notification notification = new Notification();
        notification.setRecipient(recipient);
        notification.setActor(actor);
        notification.setPost(post);
        notification.setComment(comment);
        notification.setMessage(actor.getUsername() + "님이 댓글을 좋아합니다.");
        notification.setType(NotificationType.COMMENT_LIKE);
        notificationRepository.save(notification);
    }

    public void sendPostLikeNotification(User recipient, User actor, Post post) {
        Notification notification = new Notification();
        notification.setRecipient(recipient);
        notification.setActor(actor);
        notification.setPost(post);
        notification.setMessage(actor.getUsername() + "님이 게시물을 좋아합니다.");
        notification.setType(NotificationType.POST_LIKE);
        notificationRepository.save(notification);
    }

    public void sendMessageNotification(User recipient, User actor, Post post) {
        Notification notification = new Notification();
        notification.setRecipient(recipient);
        notification.setActor(actor);
        notification.setPost(post);
        notification.setMessage(actor.getUsername() + "님이 메시지를 보냈습니다.");
        notification.setType(NotificationType.MESSAGE);
        notificationRepository.save(notification);
    }
}
