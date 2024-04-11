package com.github.devsns.domain.notifications.dto;

import org.springframework.web.socket.WebSocketSession;

public class UserWebSocketSession {
    private Long userId;
    private WebSocketSession session;

    public UserWebSocketSession(Long userId, WebSocketSession session) {
        this.userId = userId;
        this.session = session;
    }

    public Long getUserId() {
        return userId;
    }

    public WebSocketSession getSession() {
        return session;
    }

}