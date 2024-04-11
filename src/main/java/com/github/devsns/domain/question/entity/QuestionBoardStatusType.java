package com.github.devsns.domain.question.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum QuestionBoardStatusType {
    TEMP_SAVE("save"),
    SUBMIT("submit");

    private final String status;
}
