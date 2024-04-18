package com.github.devsns.domain.question.dto.read;

import com.github.devsns.domain.comments.dto.ReadAnswerCommentResDto;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class ReadAnswerDto {
    private Long questionId;
    private Long answerId;
    private Long answererId;
    private String profileImg;
    private String answerer;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    // 로그인한 사용자의 경우 답변에 대한 좋아요/싫어요 여부 포함
    private long likeCount;
    private boolean canDelete;
    private boolean isLiked;

    private List<ReadAnswerCommentResDto> answerComments;
}


