package com.github.devsns.domain.comments.controller;

import com.github.devsns.domain.comments.dto.AnswerCommentDto;
import com.github.devsns.domain.comments.dto.QuestionCommentDto;
import com.github.devsns.domain.comments.service.QuestionCommentService;
import com.github.devsns.global.component.ExtractIdUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@RestController
public class QuestionCommentController {

    private final QuestionCommentService questionCommentService;
    private final ExtractIdUtil extractIdUtil;

    public QuestionCommentController(QuestionCommentService questionCommentService, ExtractIdUtil extractIdUtil) {
        this.questionCommentService = questionCommentService;
        this.extractIdUtil = extractIdUtil;
    }

    // 질문에 댓글을 생성합니다.
    @PostMapping("/api/question/{quesId}/comment")
    public ResponseEntity<String> createQuestionComment(@PathVariable Long quesId,
                                                        @RequestBody QuestionCommentDto questionCommentDto,
                                                        Authentication authentication) {

        // Authentication 객체에서 사용자 ID 추출
        Long userId = extractIdUtil.extractUserIdFromAuthentication(authentication);

        String content = questionCommentDto.getContent(); // 댓글 내용

        // CommentService의 createComment 메서드 호출
        questionCommentService.createQuestionComment(quesId, userId, content);

        return ResponseEntity.ok("댓글 작성 완료");
    }

    @PutMapping("/api/question/{commentId}")
    public ResponseEntity<String> updateComment(@PathVariable String commentId,
                                                @RequestBody QuestionCommentDto questionCommentDto,
                                                Authentication authentication) {
        try {
            // Authentication 객체에서 사용자 ID 추출
            Long userId = extractIdUtil.extractUserIdFromAuthentication(authentication);
            String content = questionCommentDto.getContent(); // 댓글 내용

            // 댓글 소유자 확인
            questionCommentService.checkCommenter(commentId, userId);

            // 댓글 업데이트
            questionCommentService.updateQuestionComment(commentId, content);

            return ResponseEntity.ok("댓글 업데이트 성공");
        } catch (NoSuchElementException e) {
            return ResponseEntity.badRequest().body("댓글이 존재하지 않습니다");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("댓글 업데이트 실패");
        }
    }

    @DeleteMapping("/api/question/{commentId}/delete")
    public ResponseEntity<String> deleteComment(@PathVariable String commentId,
                                                Authentication authentication) {

        try {
            // Authentication 객체에서 사용자 ID 추출
            Long userId = extractIdUtil.extractUserIdFromAuthentication(authentication);

            // 댓글 소유자 확인
            questionCommentService.checkCommenter(commentId, userId);

            // 댓글을 삭제
            questionCommentService.deleteQuestionComment(commentId);

            return ResponseEntity.ok("댓글 삭제 성공");
        } catch (NoSuchElementException e) {
            return ResponseEntity.badRequest().body("댓글이 존재하지 않습니다");
        }
    }

//    @GetMapping("/api/question/{quesId}/comments")
//    public ResponseEntity<List<QuestionCommentEntity>> getQuestionComments(@PathVariable Long quesId) {
//        List<QuestionCommentEntity> comments = questionCommentService.getAllComments(quesId);
//        return ResponseEntity.ok(comments);
//    }

}
