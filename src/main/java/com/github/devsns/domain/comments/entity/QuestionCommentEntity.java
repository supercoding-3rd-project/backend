package com.github.devsns.domain.comments.entity;

import com.github.devsns.domain.question.entity.QuestionBoardEntity;
import com.github.devsns.domain.user.entitiy.UserEntity;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class QuestionCommentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ques_id", nullable = false)
    private QuestionBoardEntity question;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity commenter;

    @Column(nullable = false, updatable = false)
    private String content;

    @Column(nullable = false, updatable = false)
    @DateTimeFormat(pattern = "yy.mm.dd hh:mm")
    private LocalDateTime createdAt;

    @Column(nullable = false)
    @DateTimeFormat(pattern = "yy.mm.dd hh:mm")
    private LocalDateTime updatedAt;

    @DateTimeFormat(pattern = "yy.mm.dd hh:mm")
    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;
}


