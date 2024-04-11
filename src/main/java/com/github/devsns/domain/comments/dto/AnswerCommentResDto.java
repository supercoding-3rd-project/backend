package com.github.devsns.domain.comments.dto;

import com.github.devsns.domain.comments.entity.AnswerCommentEntity;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnswerCommentResDto {

    private Long id;
    private String content;
    private Long commenter;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public AnswerCommentResDto(AnswerCommentEntity answerCommentEntity) {
        this.id = answerCommentEntity.getId();
        this.content = answerCommentEntity.getContent();
        this.commenter = answerCommentEntity.getCommenter().getUserId();
        this.createdAt = answerCommentEntity.getCreatedAt();
        this.updatedAt = answerCommentEntity.getUpdatedAt();
    }

}
