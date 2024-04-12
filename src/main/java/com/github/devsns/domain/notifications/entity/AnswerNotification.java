package com.github.devsns.domain.notifications.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "answer_notifications")
public class AnswerNotification extends Notification {


    @Column(name = "answerer_id")
    private Long answererId; // 답변을 작성한 사용자

    @Column(name = "answerer")
    private String answerer;

    @Column(name = "question_id")
    private Long questionId; // 답변이 달린 질문

}
