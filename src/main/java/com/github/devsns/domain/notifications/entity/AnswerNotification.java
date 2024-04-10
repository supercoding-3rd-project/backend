package com.github.devsns.domain.notifications.entity;


import com.github.devsns.domain.answers.entity.AnswerEntity;
import com.github.devsns.domain.question.entity.QuestionBoardEntity;
import com.github.devsns.domain.user.entitiy.UserEntity;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Data
@Table(name = "answer_notifications")
public class AnswerNotification extends Notification {


    @Column(name = "answerer_id")
    private Long answererId; // 답변을 작성한 사용자

    @Column(name = "question_id")
    private Long questionId; // 답변이 달린 질문

}
