package com.github.devsns.domain.answers.entity;

import com.github.devsns.domain.comments.entity.AnswerCommentEntity;
import com.github.devsns.domain.question.entity.QuestionBoardEntity;
import com.github.devsns.domain.user.entitiy.UserEntity;
import com.github.devsns.global.constant.Status;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class AnswerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ques_id", nullable = false)
    private QuestionBoardEntity question;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity answerer;

    private String title;

    private String content;

    @DateTimeFormat(pattern = "yy.mm.dd hh:mm")
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @DateTimeFormat(pattern = "yy.mm.dd hh:mm")
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @DateTimeFormat(pattern = "yy.mm.dd hh:mm")
    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @OneToMany(mappedBy = "answerLike", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AnswerLike> likes = new ArrayList<>();

    @OneToMany(mappedBy ="answer", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<AnswerCommentEntity> comments = new ArrayList<>();

}
