package com.github.devsns.domain.qnas.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class QuestionBoardReqDto {

    private Integer userId;
    private String title;
    private String content;
}
