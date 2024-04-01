package com.github.devsns.domain.notifications.entity;

import com.github.devsns.domain.comments.entity.CommentEntity;
import com.github.devsns.domain.question.entity.QuestionBoardEntity;
import com.github.devsns.domain.user.userEntity.UserEntity;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class CommentNotification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private UserEntity recipient; // 알림을 받는 사용자

    @ManyToOne
    private UserEntity commenter; // 댓글을 작성한 사용자

    @ManyToOne
    private QuestionBoardEntity post; // 해당 댓글이 작성된 게시물

    @ManyToOne
    private CommentEntity comment; // 작성된 댓글

}
