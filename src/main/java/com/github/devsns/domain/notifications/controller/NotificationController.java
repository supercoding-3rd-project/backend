package com.github.devsns.domain.notifications.controller;

import com.github.devsns.domain.notifications.dto.NotificationDto;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NotificationController {

    private final SimpMessagingTemplate messagingTemplate;

    public NotificationController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @PostMapping("/notifications")
    public void sendNotification(@RequestBody NotificationDto notificationDto) {
        messagingTemplate.convertAndSend("/notifications", notificationDto);
    }
}