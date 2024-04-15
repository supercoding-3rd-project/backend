package com.github.devsns.domain.question.dto;

import com.github.devsns.domain.comments.dto.AnswerCommentResDto;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class LikeAnswerDto {
    private Long answerId;
    private long likeCount;
    private boolean isLiked;

}


