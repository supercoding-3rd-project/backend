package com.github.devsns.domain.notifications.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.devsns.domain.notifications.component.UserSessionManager;
import com.github.devsns.domain.notifications.dto.UserWebSocketSession;
import com.github.devsns.domain.notifications.entity.Notification;
import com.github.devsns.domain.notifications.repository.NotificationRepository;
import com.github.devsns.domain.user.entitiy.UserEntity;
import com.github.devsns.domain.user.repository.UserRepository;
import com.github.devsns.global.jwt.service.JwtService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Component
@Slf4j
@RequiredArgsConstructor
public class NotificationWebSocketHandler extends TextWebSocketHandler {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final NotificationRepository notificationRepository;
    private final ObjectMapper objectMapper;
    private final UserSessionManager userSessionManager;


    @Override
    @Transactional
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        super.afterConnectionEstablished(session);
        log.info("WebSocket 연결이 성공했습니다.");

        // WebSocket 연결 시 클라이언트로부터 토큰 수신
        String token = session.getHandshakeHeaders().getFirst("Authorization");
        if (token == null) {
            log.info("토큰이 포함되지 않은 요청입니다.");
            session.close(CloseStatus.BAD_DATA.withReason("토큰을 찾을 수 없어요."));
            return;
        }

        token = token.replace("Bearer ", "");
        log.info(token);


        // 토큰 검증
        if (token != null && jwtService.isTokenValid(token)) {
            log.info("토큰 검증 성공");

            // 유저 정보 추출
            Optional<String> email = jwtService.extractEmail(token);
            String emailStr = email.get();
            Optional<UserEntity> user = userRepository.findByEmail(emailStr);
            log.info("user: " + user.toString());
            Long userId = user.get().getUserId();

            UserWebSocketSession userSession = new UserWebSocketSession(userId, session);
            userSessionManager.addUserSession(userId, userSession);

            sendNotificationToUser(userSession);

        } else {
            log.info("토큰 검증 실패");
            session.close(CloseStatus.BAD_DATA.withReason("Invalid token"));
        }
    }


    @Async
    @Transactional
    public void handleNotificationCreated(Notification notification) {
        log.info("Handling NotificationCreatedEvent for notification: {}", notification.getNotificationId());
        // 나머지 로직
        Long userId = notification.getRecipientId();

        // 사용자의 WebSocket 세션을 찾아서 알림 전송
        if (userSessionManager.hasUserSession(userId)) {
            UserWebSocketSession userSession = userSessionManager.getUserSession(userId);
            sendNotificationToUser(userSession);
        }
    }

    private void sendNotificationToUser(UserWebSocketSession userSession) {
        List<Notification> notifications = notificationRepository.findAllByOrderByCreatedAtDesc();
        String jsonData = convertNotificationsToJson(notifications);
        sendNotificationJson(userSession.getSession(), jsonData);
    }

    private String convertNotificationsToJson(List<Notification> notifications) {
        try {
            return objectMapper.writeValueAsString(notifications);
        } catch (IOException e) {
            System.err.println("알림 데이터를 JSON으로 변환하는 중 오류 발생");
            throw new RuntimeException("알림 데이터를 JSON으로 변환하는 중 오류 발생", e);
        }
    }

    private void sendNotificationJson(WebSocketSession session, String jsonData) {
        try {
            session.sendMessage(new TextMessage(jsonData));
        } catch (IOException e) {
            System.err.println("알림 데이터 전송 중 오류 발생");
            try {
                session.sendMessage(new TextMessage("알림 데이터 전송 중 오류가 발생했습니다."));
            } catch (IOException ex) {
                System.err.println("오류 메시지 전송 중 오류 발생");
            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        // 사용자 ID 추출 로직
        Long userId = (Long) session.getAttributes().get("userId");

        // 맵에서 세션 제거
        userSessionManager.removeUserSession(userId);
    }

}

