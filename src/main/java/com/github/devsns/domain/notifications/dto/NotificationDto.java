package com.github.devsns.domain.notifications.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class NotificationDto {
    private String message;
//    private User recipient; // 알림을 받을 사용자 정보
//    private User sender; // 알림을 생성한 사용자 정보 (optional)

    private String likeNotification;
    private String commentNotification;
    private String followNotification;
    private String messageNotification;

    private int likeNotificationCount;
    private int commentNotificationCount;
    private int followNotificationCount;
    private int messageNotificationCount;
}
