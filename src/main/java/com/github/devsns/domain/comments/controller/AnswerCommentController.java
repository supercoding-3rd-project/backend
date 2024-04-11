package com.github.devsns.domain.comments.controller;

import com.github.devsns.domain.comments.dto.AnswerCommentReqDto;
import com.github.devsns.domain.comments.service.AnswerCommentService;
import com.github.devsns.global.component.ExtractIdUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@RestController
@RequiredArgsConstructor
public class AnswerCommentController {

    private final AnswerCommentService answerCommentService;
    private final ExtractIdUtil extractIdUtil;


    //답변에 댓글을 답니다
    @PostMapping("/api/answer/{answerId}/comment")
    public ResponseEntity<String> createAnswerComment(@PathVariable Long answerId,
                                                      @RequestBody AnswerCommentReqDto answerCommentReqDto,
                                                      Authentication authentication) {

        // Authentication 객체에서 사용자 ID 추출
        Long userId = extractIdUtil.extractUserIdFromAuthentication(authentication);

        String content = answerCommentReqDto.getContent(); // 댓글 내용

        // CommentService의 createComment 메서드 호출
        answerCommentService.createAnswerComment(answerId, userId, content);

        return ResponseEntity.ok("댓글 작성 완료");
    }

    @PutMapping("/api/answer/comment/{commentId}")
    public ResponseEntity<String> updateComment(@PathVariable String commentId,
                                                @RequestBody AnswerCommentReqDto answerCommentReqDto,
                                                Authentication authentication) {
        try {
            // Authentication 객체에서 사용자 ID 추출
            Long userId = extractIdUtil.extractUserIdFromAuthentication(authentication);
            String content = answerCommentReqDto.getContent(); // 댓글 내용

            // 댓글 소유자 확인
            answerCommentService.checkCommenter(commentId, userId);

            // 댓글 업데이트
            answerCommentService.updateAnswerComment(commentId, content);

            return ResponseEntity.ok("댓글 업데이트 성공");
        } catch (NoSuchElementException e) {
            return ResponseEntity.badRequest().body("댓글이 존재하지 않습니다");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("댓글 업데이트 실패");
        }
    }

    @DeleteMapping("/api/answer/comment/{commentId}/delete")
    public ResponseEntity<String> deleteComment(@PathVariable String commentId,
                                                Authentication authentication) {

        try {
            // Authentication 객체에서 사용자 ID 추출
            Long userId = extractIdUtil.extractUserIdFromAuthentication(authentication);

            // 댓글 소유자 확인
            answerCommentService.checkCommenter(commentId, userId);

            // 댓글을 삭제
            answerCommentService.deleteAnswerComment(commentId);

            return ResponseEntity.ok("댓글 삭제 성공");
        } catch (NoSuchElementException e) {
            return ResponseEntity.badRequest().body("댓글이 존재하지 않습니다");
        }
    }

}
