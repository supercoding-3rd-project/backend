package com.github.devsns.domain.comments.dto;

import lombok.Data;

@Data
public class CommentRequest {
    private String content; // 댓글 내용
    private Long parentCommentId; // 대댓글일 경우 상위 댓글의 ID, 상위 댓글일 경우 null
}
