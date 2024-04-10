package com.github.devsns.domain.notifications.dto;

import com.github.devsns.domain.notifications.constant.NotificationType;
import com.github.devsns.domain.user.dto.UserResponseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class NotificationDto {
    private Long answerCommentNotificationId;
    private Long answerNotificationId;
    private Long followNotificationId;
    private Long likeAnswerNotificationId;
    private Long likeQuestionNotificationId;
    private Long messageNotificationId;
    private Long questionCommentNotificationId;
    private LocalDateTime createdAt;
    private Boolean isRead;
}