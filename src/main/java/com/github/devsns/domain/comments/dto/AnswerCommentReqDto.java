package com.github.devsns.domain.comments.dto;

import lombok.Data;

@Data
public class AnswerCommentReqDto {

    private Long id;
    private String content; // 댓글 내용
}
