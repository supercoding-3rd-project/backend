package com.github.devsns.domain.notifications.entity;


import com.github.devsns.domain.answers.entity.AnswerEntity;
import com.github.devsns.domain.question.entity.QuestionBoardEntity;
import com.github.devsns.domain.user.entitiy.UserEntity;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class AnswerNotification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private UserEntity recipient; // 알림을 받는 사용자

    @ManyToOne
    private UserEntity answerer; // 답변을 작성한 사용자

    @ManyToOne
    private QuestionBoardEntity question; // 답변이 달린 질문
}
