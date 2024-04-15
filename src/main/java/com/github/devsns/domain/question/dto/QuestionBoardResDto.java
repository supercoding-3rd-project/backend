package com.github.devsns.domain.question.dto;

import com.github.devsns.domain.answers.dto.AnswerResDto;
import com.github.devsns.domain.question.entity.QuestionBoardEntity;
import com.github.devsns.global.constant.LikeType;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuestionBoardResDto {

    private Long id;
    private String title;
    private String content;
    private Long questionerId;
    private String questioner;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private long likeCount;
    private long dislikeCount;
    private List<AnswerResDto> answers;

    public QuestionBoardResDto(QuestionBoardEntity questionBoard) {
        this.id = questionBoard.getId();
        this.title = questionBoard.getTitle();
        this.content = questionBoard.getContent();
        this.questionerId = questionBoard.getQuestioner().getUserId();
        this.questioner = questionBoard.getQuestioner().getUsername();
        this.createdAt = questionBoard.getCreatedAt();
        this.updatedAt = questionBoard.getUpdatedAt();
        this.likeCount = questionBoard.getLikes().stream()
                .filter(like -> like.getLikeType().equals(LikeType.LIKE))
                .count();
        this.dislikeCount = questionBoard.getLikes().stream()
                .filter(like -> like.getLikeType().equals(LikeType.DISLIKE))
                .count();
        this.answers = questionBoard.getAnswers().stream()
                .map(AnswerResDto::new)
                .collect(Collectors.toList());
    }
}

