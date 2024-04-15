package com.github.devsns.domain.question.dto;

import com.github.devsns.domain.question.entity.TempQuestionEntity;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TempQuestionResDto {
    private Long tempId;
    private String title;
    private String content;
    private LocalDateTime createdAt;

    public static TempQuestionResDto from(TempQuestionEntity tempQuestion) {
        TempQuestionResDto tempQuestionResDto = new TempQuestionResDto();
        tempQuestionResDto.setTempId(tempQuestion.getId());
        tempQuestionResDto.setTitle(tempQuestion.getTitle());
        tempQuestionResDto.setContent(tempQuestion.getContent());
        tempQuestionResDto.setCreatedAt(tempQuestion.getCreatedAt());
        return tempQuestionResDto;
    }
}