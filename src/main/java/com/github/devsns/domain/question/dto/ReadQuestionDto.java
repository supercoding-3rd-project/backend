package com.github.devsns.domain.question.dto;

import com.github.devsns.domain.answers.dto.AnswerResDto;
import com.github.devsns.domain.question.entity.QuestionBoardEntity;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class ReadQuestionDto {
    private Long questionId;
    private Long questionerId;
    private String questioner;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    // 로그인한 사용자의 경우 질문에 대한 좋아요 여부만 포함
    private boolean isLiked;
    private boolean isDisliked;
    private long likeCount;
    private long dislikeCount;
    private List<ReadAnswerDto> answers;




}

