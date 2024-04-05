package com.github.devsns.domain.notifications.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws").withSockJS();
    }

    // "/ws" 엔드포인트를 통해 웹소켓 연결을 수신합니다.
    // SockJS를 사용하여 웹소켓이 지원되지 않는 환경에서도 폴백 옵션을 제공합니다.

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic");
        // "/topic" 프리픽스를 가진 메시지는 간단한 메모리 기반 메시지 브로커에서 처리됩니다.
        // 이는 서버에서 클라이언트로 메시지를 전송할 때 사용됩니다.

        registry.setApplicationDestinationPrefixes("/app");
        // "/app" 프리픽스를 가진 메시지는 @MessageMapping으로 매핑된 메서드에서 처리됩니다.
        // 이는 클라이언트에서 서버로 메시지를 전송할 때 사용됩니다.
    }

}