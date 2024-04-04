package com.github.devsns.domain.question.dto;

import com.github.devsns.domain.question.entity.QuestionBoardEntity;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuestionBoardResDto {

    private Long id;
    private String title;
    private String content;
    private Long likeCount;

    public QuestionBoardResDto(QuestionBoardEntity questionBoard, Long likeCount) {
        QuestionBoardResDto.builder()
                .id(questionBoard.getId())
                .title(questionBoard.getTitle())
                .content(questionBoard.getContent().getContent())
                .likeCount(likeCount)
                .build();
    }
}

