package com.github.devsns.domain.notifications.entity;

import com.github.devsns.domain.question.entity.QuestionBoardEntity;
import com.github.devsns.domain.user.entitiy.UserEntity;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class LikeQuestionNotification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private UserEntity recipient; // 알림을 받는 사용자

    @ManyToOne
    private UserEntity liker; // 좋아요를 누른 사용자

    @ManyToOne
    private QuestionBoardEntity question; // 좋아요를 누른 글
}
