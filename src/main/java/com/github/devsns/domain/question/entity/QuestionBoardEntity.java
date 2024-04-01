package com.github.devsns.domain.question.entity;

import com.github.devsns.domain.user.userEntities.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.format.annotation.DateTimeFormat;

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

    @OneToOne
    @JoinColumn(name = "content_id")
    private ContentEntity content;

    @OneToMany(mappedBy = "questionBoard")
    @JoinColumn(name = "ques_id")
    private List<LikeEntity> like = new ArrayList<>();

    @DateTimeFormat(pattern = "yy.mm.dd hh:mm")
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @DateTimeFormat(pattern = "yy.mm.dd hh:mm")
    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

//    @OneToMany(mappedBy = "questionBoard", cascade = CascadeType.PERSIST, orphanRemoval = true)
//    @JoinColumn(name = "ques_id")
//    @OnDelete(action = OnDeleteAction.CASCADE)
//    private List<AnswerEntity> answer = new ArrayList<AnswerEntity>();

    public static QuestionBoardEntity toEntity() {
        return QuestionBoardEntity.builder().build();
    }
}
