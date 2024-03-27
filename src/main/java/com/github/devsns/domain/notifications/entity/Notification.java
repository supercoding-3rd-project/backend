package com.github.devsns.domain.notifications.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "notification")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String message;
    private String likeNotification;
    private String commentNotification;
    private String followNotification;
    private String messageNotification;

    private int likeNotificationCount;
    private int commentNotificationCount;
    private int followNotificationCount;
    private int messageNotificationCount;
}
