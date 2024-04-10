package com.github.devsns.domain.notifications.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.hibernate5.support.OpenSessionInViewFilter;
import org.springframework.web.socket.config.annotation.*;

@Slf4j
@Configuration
@EnableWebSocket
public class NotificationWebSocketConfig implements WebSocketConfigurer {

    private final NotificationWebSocketHandler notificationWebSocketHandler;

    public NotificationWebSocketConfig(NotificationWebSocketHandler notificationWebSocketHandler) {
        this.notificationWebSocketHandler = notificationWebSocketHandler;
    }


    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(notificationWebSocketHandler,"/ws").setAllowedOrigins("*");
    }


    public FilterRegistrationBean<OpenSessionInViewFilter> openSessionInViewFilterRegistrationBean() {
        FilterRegistrationBean<OpenSessionInViewFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new OpenSessionInViewFilter());
        registrationBean.addUrlPatterns("/ws/*"); // 웹소켓 요청의 URL 패턴에 따라 조정
        return registrationBean;
    }
}

