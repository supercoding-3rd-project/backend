package com.github.devsns.domain.answers.dto;

import com.github.devsns.domain.answers.entity.AnswerEntity;
import com.github.devsns.domain.comments.dto.AnswerCommentResDto;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnswerResDto {

    private Long id;
    private String content;
    private Long answererId;
    private String answerer;
    private Long likeCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<AnswerCommentResDto> comments;

    public AnswerResDto(AnswerEntity answerEntity) {
        this.id = answerEntity.getId();
        this.content = answerEntity.getContent();
        this.answererId = answerEntity.getAnswerer().getUserId();
        this.answerer = answerEntity.getAnswerer().getUsername();
        this.createdAt = answerEntity.getCreatedAt();
        this.updatedAt = answerEntity.getUpdatedAt();
        this.likeCount = (long) answerEntity.getLikes().size();
        this.comments = answerEntity.getComments().stream()
                .map(AnswerCommentResDto::new)
                .collect(Collectors.toList());
    }

}