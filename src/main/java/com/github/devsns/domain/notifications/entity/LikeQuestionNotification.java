package com.github.devsns.domain.notifications.entity;

import com.github.devsns.domain.question.entity.QuestionBoardEntity;
import com.github.devsns.domain.user.entitiy.UserEntity;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Data
@Table(name = "like_question_notification")
public class LikeQuestionNotification extends Notification {

    @Column(name = "liker_id")
    private Long likerId; // 좋아요를 누른 사용자

    @Column(name = "question_id")
    private Long questionId; // 좋아요를 누른 글
}
