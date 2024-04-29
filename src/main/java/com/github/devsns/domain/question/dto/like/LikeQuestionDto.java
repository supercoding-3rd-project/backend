package com.github.devsns.domain.question.dto.like;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class LikeQuestionDto {
    private Long questionId;
    private boolean isLiked;
    private boolean isDisliked;
    private long likeCount;
    private long dislikeCount;





}

