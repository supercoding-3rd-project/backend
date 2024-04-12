package com.github.devsns.domain.question.dto;

import com.github.devsns.domain.answers.dto.AnswerResDto;
import com.github.devsns.domain.question.entity.QuestionBoardEntity;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SearchQuestionDto {

    private Long id;
    private String title;
    private String content;
    private Long questionerId;
    private String questioner;
    private LocalDateTime createdAt;
    private Long likeCount;
    private List<AnswerResDto> answers;

    public SearchQuestionDto(QuestionBoardEntity questionBoard) {
        this.id = questionBoard.getId();
        this.title = questionBoard.getTitle();
        this.content = questionBoard.getContent();
        this.questionerId = questionBoard.getUser().getUserId();
        this.questioner = questionBoard.getUser().getUsername();
        this.createdAt = questionBoard.getCreatedAt();
        this.likeCount = (long) questionBoard.getLike().size();
        this.answers = questionBoard.getAnswer().stream()
                .map(AnswerResDto::new)
                .collect(Collectors.toList());
    }

}

