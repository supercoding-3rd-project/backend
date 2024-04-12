package com.github.devsns.domain.question.entity;

import com.github.devsns.domain.answers.entity.AnswerEntity;
import com.github.devsns.domain.question.dto.QuestionBoardReqDto;
import com.github.devsns.domain.user.entitiy.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "question_board")
public class QuestionBoardEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ques_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private UserEntity user;

    @Column(name = "title")
    private String title;

    @Column(name = "content")
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private QuestionBoardStatusType statusType;

    @OneToMany(mappedBy = "questionBoard")
    private List<LikeEntity> like = new ArrayList<>();

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;


    @OneToMany(mappedBy = "questionBoard", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<AnswerEntity> answer = new ArrayList<>();

    private void setStatusType(QuestionBoardStatusType statusType) {
        this.statusType = statusType;
    }

    public static QuestionBoardEntity toEntity(UserEntity user, QuestionBoardReqDto questionBoardReqDto) {
        QuestionBoardEntity questionBoard = builder()
                .user(user)
                .title(questionBoardReqDto.getTitle())
                .content(questionBoardReqDto.getContent())
                .createdAt(LocalDateTime.now())
                .build();

        if (questionBoardReqDto.getStatusType().equals(QuestionBoardStatusType.SUBMIT.getStatus())) {
            questionBoard.setStatusType(QuestionBoardStatusType.SUBMIT);
        } else {
            questionBoard.setStatusType(QuestionBoardStatusType.TEMP_SAVE);
        }

        return questionBoard;
    }
}
