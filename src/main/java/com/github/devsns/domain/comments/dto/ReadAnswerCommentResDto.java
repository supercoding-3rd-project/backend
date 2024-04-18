package com.github.devsns.domain.comments.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReadAnswerCommentResDto {

    private Long commentId;
    private String content;
    private Long commenterId;
    private String commenter;
    private String profileImage;
    private boolean canDelete;
    private LocalDateTime createdAt;


}
