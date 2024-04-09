package com.github.devsns.domain.question.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum QuestionBoardStatusType {
    TEMP_SAVE("save"),
    SUBMIT("submit");

    private final String status;
}
