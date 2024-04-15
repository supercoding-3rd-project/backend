package com.github.devsns.domain.notifications.entity;

import com.github.devsns.domain.notifications.component.NotificationEntityListener;
import com.github.devsns.domain.notifications.constant.NotificationType;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Entity
@Component
@EntityListeners(NotificationEntityListener.class)
@Data
@Table(name = "notifications")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id")
    private Long notificationId;

    @Enumerated(EnumType.STRING)
    private NotificationType type;

    @Column(name = "recipient_id")
    private Long recipientId;


    @Column(name="recipient")
    private String recipient;

    @Column(nullable = false, updatable = false)
    @CreatedDate
    private LocalDateTime createdAt;

    private boolean isRead = false;

}