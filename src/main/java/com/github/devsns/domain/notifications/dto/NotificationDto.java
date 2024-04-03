package com.github.devsns.domain.notifications.dto;

import com.github.devsns.domain.notifications.constant.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class NotificationDto {

    private Long id;
    private Long userId;
    private LocalDateTime createdAt;
    private NotificationType type;
}
