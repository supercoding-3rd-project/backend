package com.github.devsns.domain.notifications.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.devsns.domain.notifications.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
public class WebSocketHandler extends TextWebSocketHandler {

    @Autowired
    private NotificationService notificationService;

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // 클라이언트로부터 메시지를 받았을 때의 처리 로직
        String payload = message.getPayload();

        // 토큰 추출
        String token = extractTokenFromPayload(payload);

        // 토큰을 사용하여 인증 및 유저 ID 확인
        Long userId = authenticationService.getUserIdFromToken(token); //가상의 서비스

        // 사용자에게 최근 알림을 전송
        notificationService.sendRecentNotificationsToUser(userId, session);
    }

    private String extractTokenFromPayload(String payload) {
        // JWT 토큰의 일반적인 형식은 "Bearer {token}"입니다.
        // 따라서 공백을 기준으로 문자열을 분리하여 토큰 부분을 추출합니다.
        String[] parts = payload.split(" ");

        // 분리된 부분 중에서 두 번째 요소가 토큰이 됩니다.
        // 예를 들어, "Bearer {token}" 형식이면 parts[1]에 토큰이 위치합니다.
        if (parts.length == 2) {
            return parts[1];
        } else {
            // 형식이 맞지 않는 경우 null을 반환하거나 예외 처리를 수행할 수 있습니다.
            throw new IllegalArgumentException("Invalid token format");
        }
    }
}