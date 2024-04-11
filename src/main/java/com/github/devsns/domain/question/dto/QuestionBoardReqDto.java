package com.github.devsns.domain.question.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class QuestionBoardReqDto {

    private String title;
    private String content;
    private String statusType;
}
