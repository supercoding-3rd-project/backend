package com.github.devsns.domain.question.dto.like;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LikeAnswerDto {
    private Long answerId;
    private long likeCount;
    private boolean isLiked;

}


