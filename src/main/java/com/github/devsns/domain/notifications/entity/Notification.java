package com.github.devsns.domain.notifications.entity;

import com.github.devsns.domain.notifications.constant.NotificationType;
import com.github.devsns.domain.user.entitiy.UserEntity;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "notifications")
@EntityListeners(AuditingEntityListener.class)
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "recipient_id", referencedColumnName = "user_id")
    private UserEntity recipient; // 로그인한 유저

    @Enumerated(EnumType.STRING)
    private NotificationType type;

    @ManyToOne(cascade = CascadeType.ALL)
    private QuestionCommentNotification questionCommentNotification; // 질문 댓글 작성 알림

    @ManyToOne(cascade = CascadeType.ALL)
    private AnswerCommentNotification answerCommentNotification; // 답변 댓글 작성 알림

    @ManyToOne(cascade = CascadeType.ALL)
    private LikeQuestionNotification likeQuestionNotification; // 내 질문 좋아요 알림

    @ManyToOne(cascade = CascadeType.ALL)
    private LikeAnswerNotification likeAnswerNotification; // 내 답변 좋아요 알림

    @ManyToOne(cascade = CascadeType.ALL)
    private MessageNotification messageNotification; // 메시지 알림

    @ManyToOne(cascade = CascadeType.ALL)
    private AnswerNotification answerNotification; // 답변 알림

    @ManyToOne(cascade = CascadeType.ALL)
    private FollowNotification followNotification; // 팔로잉 알림
    

    @Column(nullable = false, updatable = false)
    @CreatedDate
    private LocalDateTime createdAt;

    private boolean isRead = false;
}
