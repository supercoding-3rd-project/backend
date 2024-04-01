package com.github.devsns.domain.question.entity;

import com.github.devsns.domain.user.userEntities.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "ques_like")
public class LikeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "like_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "ques_id")
    private QuestionBoardEntity questionBoard;

    public static LikeEntity toEntity(UserEntity user, QuestionBoardEntity questionBoard) {
        return LikeEntity.builder()
                .user(user)
                .questionBoard(questionBoard)
                .build();
    }
}
