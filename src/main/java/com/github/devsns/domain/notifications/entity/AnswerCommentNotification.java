package com.github.devsns.domain.notifications.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "answer_comment_notification")
public class AnswerCommentNotification extends Notification {

    @Column(name = "commenter_id")
    private Long commenterId; // 댓글을 작성한 사용자

    @Column(name = "commenter")
    private String commenter;

    @Column(name = "answer_id")
    private Long answerId; // 해당 댓글이 작성된 답변

    @Column(name = "comment_id")
    private Long commentId; // 작성된 댓글

}
