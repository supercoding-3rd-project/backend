package com.github.devsns.domain.notifications.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class CommentNotification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User recipient; // 알림을 받는 사용자

    @ManyToOne
    private User commenter; // 댓글을 작성한 사용자

    @ManyToOne
    private Post post; // 해당 댓글이 작성된 게시물

    @ManyToOne
    private Comment comment; // 작성된 댓글
}
