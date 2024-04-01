package com.github.devsns.domain.notifications.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.devsns.domain.comments.entity.CommentEntity;
import com.github.devsns.domain.notifications.constant.NotificationType;
import com.github.devsns.domain.notifications.entity.*;
import com.github.devsns.domain.notifications.repository.NotificationRepository;
import com.github.devsns.domain.user.userEntity.UserEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.List;

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

    public void sendCommentNotification(UserEntity postAuthor, CommentEntity comment) {
        Notification notification = new Notification();
        notification.setRecipient(postAuthor);

        UserEntity commenter = comment.getCommenter();
        Post post = comment.getPost();

        CommentNotification commentNotification = new CommentNotification();
        commentNotification.setCommenter(commenter);
        commentNotification.setPost(post);
        commentNotification.setComment(comment);

        notification.setCommentNotification(commentNotification);
        notification.setType(NotificationType.COMMENT);
        notificationRepository.save(notification);
    }

    public void sendCommentLikeNotification(UserEntity recipient, UserEntity liker, CommentEntity comment) {
        Notification notification = new Notification();
        notification.setRecipient(recipient);


        CommentLikeNotification commentLikeNotification = new CommentLikeNotification();
        commentLikeNotification.setLiker(liker);
        commentLikeNotification.setComment(comment);

        notification.setCommentLikeNotification(commentLikeNotification);
        notification.setType(NotificationType.COMMENT_LIKE);
        notificationRepository.save(notification);
    }

    public void sendPostLikeNotification(UserEntity recipient, UserEntity liker, Post post) {
        Notification notification = new Notification();
        notification.setRecipient(recipient);

        PostLikeNotification postLikeNotification = new PostLikeNotification();
        postLikeNotification.setLiker(liker);
        postLikeNotification.setPost(post);

        notification.setPostLikeNotification(postLikeNotification);
        notification.setType(NotificationType.POST_LIKE);
        notificationRepository.save(notification);
    }

    public void sendMessageNotification(UserEntity recipient, UserEntity sender) {
        Notification notification = new Notification();
        notification.setRecipient(recipient);

        MessageNotification messageNotification = new MessageNotification();
        messageNotification.setSender(sender);

        notification.setType(NotificationType.MESSAGE);
        notificationRepository.save(notification);
    }
}
