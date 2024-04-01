package com.github.devsns.domain.notifications.entity;

import com.github.devsns.domain.comments.entity.CommentEntity;
import com.github.devsns.domain.user.userEntities.UserEntity;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class CommentLikeNotification  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private UserEntity recipient; // 알림을 받는 사용자

    @ManyToOne
    private UserEntity liker; // 댓글 좋아요를 누른 사용자

    @ManyToOne
    private CommentEntity comment; // 좋아요를 누른 댓글
}
