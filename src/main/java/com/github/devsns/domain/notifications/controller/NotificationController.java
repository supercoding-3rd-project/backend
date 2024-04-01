//package com.github.devsns.domain.notifications.controller;
//
//import com.github.devsns.domain.notifications.dto.NotificationDto;
//import com.github.devsns.domain.notifications.entity.Notification;
//import com.github.devsns.domain.notifications.service.NotificationService;
//import org.springframework.messaging.simp.SimpMessagingTemplate;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//public class NotificationController {
//    private final NotificationService notificationService;
//    private final SimpMessagingTemplate messagingTemplate;
//
//    public NotificationController(NotificationService notificationService, SimpMessagingTemplate messagingTemplate) {
//        this.notificationService = notificationService;
//        this.messagingTemplate = messagingTemplate;
//    }
//
//    @GetMapping("/api/notifications")
//    public void sendRecentNotifications() {
//        List<NotificationDto> recentNotifications = notificationService.sendNotificationToUser(1L); // 최신 10개 알림을 가져옵니다.
//        messagingTemplate.convertAndSend("/topic/notifications", recentNotifications); // 최신 알림을 클라이언트에게 전송합니다.
//    }
//}

//로그인 성공 시 아래 주입해서 실행하도록 하기.
// notificationService.sendNotificationToUser(userId);
