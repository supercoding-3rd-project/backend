package com.github.devsns.domain.question.dto;

import com.github.devsns.domain.question.entity.QuestionBoardStatusType;
import lombok.*;

@Getter
@Setter
@Builder
public class QuestionBoardReqDto {

    private String title;
    private String content;
    private String statusType;
}
