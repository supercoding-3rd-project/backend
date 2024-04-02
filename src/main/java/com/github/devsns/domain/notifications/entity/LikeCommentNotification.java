package com.github.devsns.domain.notifications.entity;

import com.github.devsns.domain.comments.entity.AnswerCommentEntity;
import com.github.devsns.domain.comments.entity.QuestionCommentEntity;
import com.github.devsns.domain.user.entitiy.UserEntity;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class LikeCommentNotification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private UserEntity recipient; // 알림을 받는 사용자

    @ManyToOne
    private UserEntity liker; // 댓글 좋아요를 누른 사용자

    @ManyToOne
    private QuestionCommentEntity questionComment; // question 좋아요를 누른 댓글

    @ManyToOne
    private AnswerCommentEntity answerComment; // answer 좋아요 누른 댓글
}
