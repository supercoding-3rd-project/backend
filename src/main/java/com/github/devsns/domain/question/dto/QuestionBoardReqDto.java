package com.github.devsns.domain.question.dto;

import lombok.*;

@Getter
@Setter
@Builder
public class QuestionBoardReqDto {

    private String title;
    private String content;
}
