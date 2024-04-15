package com.github.devsns.domain.question.dto;

import com.github.devsns.domain.answers.dto.AnswerResDto;
import com.github.devsns.domain.comments.dto.AnswerCommentResDto;
import com.github.devsns.domain.question.entity.QuestionBoardEntity;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class ReadAnswerDto {
    private Long questionId;
    private Long answerId;
    private Long answererId;
    private String answerer;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    // 로그인한 사용자의 경우 답변에 대한 좋아요/싫어요 여부 포함
    private long likeCount;
    private boolean isLiked;


    private List<AnswerCommentResDto> answerComments;
}


