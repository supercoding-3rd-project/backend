package com.github.devsns.domain.chat.websocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;
import org.springframework.web.socket.handler.WebSocketHandlerDecorator;

@Configuration
@EnableWebSocketMessageBroker
@Slf4j
public class ChatWebSocketConfig implements WebSocketMessageBrokerConfigurer {


    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/chat").withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // "/app"으로 시작하는 메시지가 메시지 처리 메서드로 라우팅되도록 설정합니다.
        registry.setApplicationDestinationPrefixes("/app");
        // "/topic"으로 시작하는 메시지가 메시지 브로커로 라우팅되도록 설정합니다.
        registry.enableSimpleBroker("/topic","/queue");
    }



    @Override
    public void configureWebSocketTransport(WebSocketTransportRegistration registration) {
        registration.addDecoratorFactory(this::decorateHandler);
    }

    private WebSocketHandlerDecorator decorateHandler(WebSocketHandler handler) {
        return new WebSocketHandlerDecorator(handler) {
            @Override
            public void afterConnectionEstablished(WebSocketSession session) throws Exception {
                logConnectionEstablished(session);
                super.afterConnectionEstablished(session);
            }

            @Override
            public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
                logConnectionClosed(session);
                super.afterConnectionClosed(session, closeStatus);
            }
        };
    }

    private void logConnectionEstablished(WebSocketSession session) {
        log.info("WebSocket session established: {}", session.getId());
        // 예시: 사용자에게 환영 메시지 전송
        // String username = extractUsername(session);
        // if (username != null) {
        //     messagingTemplate.convertAndSendToUser(username, "/queue/messages", "Welcome!");
        // }
    }

    private void logConnectionClosed(WebSocketSession session) {
        log.info("WebSocket session closed: {}", session.getId());
    }

    // 이 메서드는 예시로 남겨두되, 실제 사용 여부는 비즈니스 요구사항에 따라 결정
    private String extractUsername(WebSocketSession session) {
        return (String) session.getAttributes().get("username");
    }
}
