package com.github.devsns.domain.question.dto;

import com.github.devsns.domain.question.entity.TempQuestionEntity;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TempQuestionReqDto {
    private String title;
    private String content;

}