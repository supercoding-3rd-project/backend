package com.github.devsns.domain.notifications.entity;

import com.github.devsns.domain.answers.entity.AnswerEntity;
import com.github.devsns.domain.question.entity.QuestionBoardEntity;
import com.github.devsns.domain.user.entitiy.UserEntity;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Data
@Table(name = "like_answer_notification")
public class LikeAnswerNotification extends Notification {

    @Column(name = "liker_id")
    private Long likerId; // 좋아요를 누른 사용자

    @Column(name = "answer_id")
    private Long answerId; // 좋아요를 누른 글

}
