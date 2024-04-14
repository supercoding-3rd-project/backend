package com.github.devsns.domain.comments.controller;

import com.github.devsns.domain.comments.dto.AnswerCommentReqDto;
import com.github.devsns.domain.comments.service.AnswerCommentService;
import com.github.devsns.global.component.ExtractUserDataUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "답변 댓글 API", description = "댓글 C, U, D")
public class AnswerCommentController {

    private final AnswerCommentService answerCommentService;
    private final ExtractUserDataUtil extractUserDataUtil;


    //답변에 댓글을 답니다
    @Operation(summary = "답변 댓글 생성")
    @PostMapping("/v1/answer/{answerId}/comment/create")
    public ResponseEntity<String> createAnswerComment(@PathVariable Long answerId,
                                                      @RequestBody AnswerCommentReqDto answerCommentReqDto,
                                                      Authentication authentication) {

        // Authentication 객체에서 사용자 ID 추출
        Long userId = extractUserDataUtil.extractUserIdFromAuthentication(authentication);

        String content = answerCommentReqDto.getContent(); // 댓글 내용

        // CommentService의 createComment 메서드 호출
        answerCommentService.createAnswerComment(answerId, userId, content);

        return ResponseEntity.ok("댓글 작성 완료");
    }
    @Operation(summary = "답변 댓글 수정")
    @PutMapping("/v1/answer/comment/update/{commentId}")
    public ResponseEntity<String> updateComment(@PathVariable String commentId,
                                                @RequestBody AnswerCommentReqDto answerCommentReqDto,
                                                Authentication authentication) {
        try {
            // Authentication 객체에서 사용자 ID 추출
            Long userId = extractUserDataUtil.extractUserIdFromAuthentication(authentication);
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
    @Operation(summary = "답변 댓글 삭제")
    @DeleteMapping("/api/answer/comment/delete/{commentId}")
    public ResponseEntity<String> deleteComment(@PathVariable String commentId,
                                                Authentication authentication) {

        try {
            // Authentication 객체에서 사용자 ID 추출
            Long userId = extractUserDataUtil.extractUserIdFromAuthentication(authentication);

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
