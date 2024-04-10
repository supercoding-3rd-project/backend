package com.github.devsns.domain.notifications.entity;

import com.github.devsns.domain.answers.entity.AnswerEntity;
import com.github.devsns.domain.comments.entity.QuestionCommentEntity;
import com.github.devsns.domain.question.entity.QuestionBoardEntity;
import com.github.devsns.domain.user.entitiy.UserEntity;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Data
@Table(name = "question_comment_notification")
public class QuestionCommentNotification extends Notification {

    @Column(name = "commenter_id")
    private Long commenterId; // 댓글을 작성한 사용자

    @Column(name = "question_id")
    private Long questionId; // 해당 댓글이 작성된 질문

    @Column(name = "comment_id")
    private Long commentId; // 작성된 댓글

}
