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

    private Long commentId;
    private String content;
    private Long commenterId;
    private String commenter;
    private String profileImage;
    private LocalDateTime createdAt;

    public AnswerCommentResDto(AnswerCommentEntity answerCommentEntity) {
        this.commentId = answerCommentEntity.getId();
        this.content = answerCommentEntity.getContent();
        this.commenterId = answerCommentEntity.getCommenter().getUserId();
        this.commenter = answerCommentEntity.getCommenter().getUsername();
        this.profileImage = answerCommentEntity.getCommenter().getImageUrl();
        this.createdAt = answerCommentEntity.getCreatedAt();
    }

}
