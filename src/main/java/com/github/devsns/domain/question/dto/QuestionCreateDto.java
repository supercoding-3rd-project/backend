package com.github.devsns.domain.question.dto;

import lombok.Data;

import java.util.List;

@Data
public class QuestionCreateDto {
    private String message;
    private List<TempQuestionResDto> temporarySavedQuestions;

    public QuestionCreateDto(String message, List<TempQuestionResDto> temporarySavedQuestions) {
        this.message = message;
        this.temporarySavedQuestions = temporarySavedQuestions;
    }


}
