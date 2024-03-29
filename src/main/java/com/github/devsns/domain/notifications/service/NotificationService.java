package com.github.devsns.domain.notifications.service;

import org.springframework.web.socket.WebSocketSession;

public interface NotificationService {
    public void sendRecentNotificationsToUser(Long userId, WebSocketSession session);

}
