package com.github.devsns.domain.answers.dto;

import lombok.Data;

@Data
public class AnswerReqDto {
    private Long id;
    private Long userId;
    private Long questionId;
    private String title;
    private String content;
}
