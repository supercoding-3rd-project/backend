package com.github.devsns.domain.notifications.service;

import com.github.devsns.domain.comments.entity.CommentEntity;
import com.github.devsns.domain.user.entitiy.UserEntity;
import org.springframework.web.socket.WebSocketSession;

public interface NotificationService {
    void sendRecentNotificationsToUser(Long userId, WebSocketSession session);

    void sendCommentNotification(UserEntity postAuthor, CommentEntity comment);
}
