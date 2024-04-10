package com.github.devsns.domain.notifications.component;

import com.github.devsns.domain.notifications.dto.UserWebSocketSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
public class UserSessionManager {
    private final Map<Long, UserWebSocketSession> userSessionMap = new ConcurrentHashMap<>();

    public void addUserSession(Long userId, UserWebSocketSession userSession) {
        userSessionMap.put(userId, userSession);
    }

    public void removeUserSession(Long userId) {
        userSessionMap.remove(userId);
    }

    public UserWebSocketSession getUserSession(Long userId) {
        return userSessionMap.get(userId);
    }

    public boolean hasUserSession(Long userId) {
        return userSessionMap.containsKey(userId);
    }
}