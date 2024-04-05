package com.github.devsns.domain.notifications.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.devsns.domain.notifications.component.NotificationCreatedEvent;
import com.github.devsns.domain.notifications.entity.Notification;
import com.github.devsns.domain.notifications.repository.NotificationRepository;
import com.github.devsns.domain.notifications.service.NotificationService;
import com.github.devsns.domain.user.entitiy.UserEntity;
import com.github.devsns.domain.user.repository.UserRepository;
import com.github.devsns.global.jwt.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class WebSocketHandler extends TextWebSocketHandler {


    private NotificationService notificationService;
    private JwtService jwtService;
    private UserRepository userRepository;
    private NotificationRepository notificationRepository;
    private ObjectMapper objectMapper;

    // userSessionMap
    // key : userId
    // value : WebSocketSession
    private final Map<Long, WebSocketSession> userSessionMap = new ConcurrentHashMap<>();

    @Autowired
    public WebSocketHandler(NotificationService notificationService, JwtService jwtService,
                            UserRepository userRepository, NotificationRepository notificationRepository,
                            ObjectMapper objectMapper) {
        this.notificationRepository = notificationRepository;
        this.notificationService = notificationService;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.objectMapper = objectMapper;
    }


    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        super.afterConnectionEstablished(session);
        System.out.println("WebSocket 연결이 성공했습니다.");

        // WebSocket 연결 시 클라이언트로부터 토큰 수신
        String token = extractTokenFromSession((String)session.getAttributes().get("token"));

        // 토큰 검증
        if (token != null && jwtService.isTokenValid(token)) {
            System.out.println("토큰 검증 성공");

            // 유저 정보 추출
            Optional<String> email = jwtService.extractEmail(token);
            Optional<UserEntity> user = userRepository.findByEmail(email.get());
            Long userId = user.get().getUserId();

            // 사용자 ID를 세션 속성에 저장
            session.getAttributes().put("userId", userId);

            // 사용자의 WebSocket 세션 맵에 추가
            userSessionMap.put(userId, session);

            // 사용자의 알림 데이터 전송
            sendNotificationToUser(session, userId);
        } else {
            System.out.println("토큰 검증 실패");
            // 토큰이 유효하지 않은 경우 연결 종료 및 오류 메시지 전송
            session.close(CloseStatus.BAD_DATA.withReason("Invalid token"));
        }
    }

    @EventListener
    public void handleNotificationCreated(NotificationCreatedEvent event) {
        Notification notification = event.getNotification();
        Long userId = notification.getRecipient().getUserId();

        // 사용자의 WebSocket 세션을 찾아서 알림 전송
        WebSocketSession session = findSessionByUserId(userId);
        if (session != null && session.isOpen()) {
            sendNotificationToUser(session, userId);
        }
    }

    private void sendNotificationToUser(WebSocketSession session, Long userId) {
        List<Notification> notifications = getNotificationsForUser(userId);
        String jsonData = convertNotificationsToJson(notifications);
        sendNotificationJson(session, jsonData);
    }

    private List<Notification> getNotificationsForUser(Long userId) {
        return notificationRepository.findAllByRecipientUserIdOrderByCreatedAtDesc(userId);
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

    private WebSocketSession findSessionByUserId(Long userId) {
        // userId에 해당하는 세션이 있는 경우 반환하고, 없으면 null 반환
        return userSessionMap.computeIfPresent(userId, (key, session) -> session);
    }


    private String extractTokenFromSession(String token) {
        // JWT 토큰의 일반적인 형식은 "Bearer {token}"입니다.
        // 따라서 공백을 기준으로 문자열을 분리하여 토큰 부분을 추출합니다.

        String[] parts = token.split(" ");

        // 분리된 부분 중에서 두 번째 요소가 토큰이 됩니다.
        // 예를 들어, "Bearer {token}" 형식이면 parts[1]에 토큰이 위치합니다.
        if (parts.length == 2) {
            jwtService.isTokenValid(parts[1]);
            return parts[1];
        } else {
            // 형식이 맞지 않는 경우 null을 반환하거나 예외 처리를 수행할 수 있습니다.
            throw new IllegalArgumentException("Invalid token format");
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        // 사용자 ID 추출 로직
        Long userId = (Long) session.getAttributes().get("userId");

        // 맵에서 세션 제거
        userSessionMap.remove(userId);
    }

}

