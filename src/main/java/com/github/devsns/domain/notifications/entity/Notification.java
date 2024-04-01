package com.github.devsns.domain.notifications.entity;

import com.github.devsns.domain.notifications.constant.NotificationType;
import com.github.devsns.domain.user.userEntities.UserEntity;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "notifications")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private UserEntity recipient; // 로그인한 유저

    @Enumerated(EnumType.STRING)
    private NotificationType type; // COMMENT,

    @ManyToOne
    private CommentLikeNotification commentLikeNotification; // 댓글 좋아요 알림

    @ManyToOne
    private CommentNotification commentNotification; // 댓글 작성 알림

    @ManyToOne
    private PostLikeNotification postLikeNotification; // 내 글 좋아요 알림

    @ManyToOne
    private MessageNotification messageNotification; // 메시지 알림

//    @ManyToOne
//    private UserEntity tagger; // 태그한 사람 (TagNotification에서만 사용)

    @Column(nullable = false)
    private LocalDateTime createdAt;

    private boolean isRead = false;
}
