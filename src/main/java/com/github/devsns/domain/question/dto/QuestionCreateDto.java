package com.github.devsns.domain.question.dto;

import lombok.Data;

import java.util.List;

@Data
public class QuestionCreateDto {
    private String message;
    private Long questionId;

    public QuestionCreateDto(String message, Long questionId) {
        this.message = message;
        this.questionId = questionId;
    }

}
