package com.github.devsns.domain.comments.entity;

import com.github.devsns.domain.user.entitiy.UserEntity;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Entity
@Data
public class QuestionLikeComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_comment_id", nullable = false)
    private QuestionCommentEntity questionComment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Column(nullable = false, updatable = false)
    @DateTimeFormat(pattern = "yy.mm.dd hh:mm")
    private LocalDateTime createdAt;

}
