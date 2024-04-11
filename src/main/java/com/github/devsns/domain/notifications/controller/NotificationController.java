package com.github.devsns.domain.notifications.controller;

import com.github.devsns.domain.notifications.dto.NotificationRequest;
import com.github.devsns.domain.notifications.entity.Notification;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@Controller
public class NotificationController {

    @Autowired
    private EntityManager entityManager;

    @PostMapping("/notification/read")
    @Transactional
    public ResponseEntity<String> readNotification(@RequestBody NotificationRequest request) {
        Long notificationId = request.getId();
        Notification notification = entityManager.find(Notification.class, notificationId);
        if (notification != null) {
            if (notification.isRead()) {
                return ResponseEntity.badRequest().body("이미 읽은 알림입니다.");
            }
            notification.setRead(true);
            entityManager.merge(notification);
            return ResponseEntity.ok("해당 알림을 읽었습니다.");
        } else {
            return ResponseEntity.badRequest().body("알림을 찾을 수 없습니다.");
        }
    }
}
