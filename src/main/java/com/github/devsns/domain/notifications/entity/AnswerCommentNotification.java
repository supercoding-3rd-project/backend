package com.github.devsns.domain.notifications.entity;

import com.github.devsns.domain.answers.entity.AnswerEntity;
import com.github.devsns.domain.comments.entity.AnswerCommentEntity;
import com.github.devsns.domain.comments.entity.QuestionCommentEntity;
import com.github.devsns.domain.user.entitiy.UserEntity;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class AnswerCommentNotification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private UserEntity recipient; // 알림을 받는 사용자

    @ManyToOne
    private UserEntity commenter; // 댓글을 작성한 사용자

    @ManyToOne
    private AnswerEntity answer; // 해당 댓글이 작성된 답변

    @ManyToOne
    private AnswerCommentEntity comment; // 작성된 댓글

}
