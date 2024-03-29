package com.github.devsns.domain.notifications.entity;

import com.github.devsns.domain.notifications.constant.NotificationType;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User recipient; // 로그인한 유저

    @Enumerated(EnumType.STRING)
    private NotificationType type; // COMMENT,

    @ManyToOne
    private Comment comment; // 댓글 생성 및 댓글 좋아요

    @ManyToOne
    private Post post; // 내 글 좋아요

    @ManyToOne
    private User sender; // 보낸 사람 (MessageNotification에서만 사용)

    @ManyToOne
    private User tagger; // 태그한 사람 (TagNotification에서만 사용)
    @Column(nullable = false)
    private LocalDateTime createdAt;


}
