package com.github.devsns.domain.comments.controller;

import com.github.devsns.domain.comments.dto.CommentRequest;
import com.github.devsns.domain.comments.service.CommentService;
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.Jwts;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@RestController
public class QuestionCommentController {

    private final CommentService commentService;

    public QuestionCommentController(CommentService commentService) {
        this.commentService = commentService;
    }

//    // JWT에서 사용자 ID 추출하는 메서드
//    public Long extractUserIdFromJwt(String jwtToken) {
//        // 토큰 발급 시 사용한 암호화 키
//        String secretKey = "your-secret-key";
//
//        // JWT 토큰에서 payload 추출
//        Claims claims = Jwts.parser()
//                .setSigningKey(secretKey.getBytes())
//                .parseClaimsJws(jwtToken)
//                .getBody();
//
//        // payload에서 사용자 ID 추출
//        return claims.get("userId", Long.class);
//    }
//
//    // 질문에 댓글을 생성합니다.
//    @PostMapping("/api/question/{quesId}/comment")
//    public ResponseEntity<String> createQuestionComment(@PathVariable Long quesId,
//                                                @RequestBody CommentRequest commentRequest,
//                                                @RequestHeader("Authorization") String jwtToken) {
//
//        // JWT에서 사용자 ID 추출
//        Long userId = extractUserIdFromJwt(jwtToken);
//
//        String content = commentRequest.getContent(); // 댓글 내용
//        Long parentCommentId = commentRequest.getParentCommentId(); // 상위 댓글 확인
//
//        // CommentService의 createComment 메서드 호출
//        commentService.createQuestionComment(quesId, userId, content, parentCommentId);
//
//        return ResponseEntity.ok("댓글 작성 완료");
//    }
//
//
//    // 댓글을 업데이트합니다.
//    @PutMapping("/api/question/{commentId}")
//    public ResponseEntity<String> updateComment(@PathVariable String commentId,
//                                                @RequestBody CommentRequest commentRequest,
//                                                @RequestHeader("Authorization") String jwtToken) {
//        try {
//            // JWT에서 사용자 ID 추출
//            Long userId = extractUserIdFromJwt(jwtToken);
//            String content = commentRequest.getContent(); // 댓글 내용
//
//            // 댓글 소유자 확인
//            commentService.checkCommenter(commentId, userId);
//
//            // 댓글 업데이트
//            commentService.updateComment(commentId, content);
//
//            return ResponseEntity.ok("댓글 업데이트 성공");
//        } catch (NoSuchElementException e) {
//            return ResponseEntity.badRequest().body("댓글이 존재하지 않습니다");
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().body("댓글 업데이트 실패");
//        }
//    }
//
//    // 댓글에 좋아요를 추가합니다.
//    @PostMapping("/api/question/{commentId}/like")
//    public ResponseEntity<String> likeComment(@PathVariable String commentId,
//                                             @RequestHeader("Authorization") String jwtToken) {
//
//        try
//        {
//            // JWT에서 사용자 ID 추출
//            Long userId = extractUserIdFromJwt(jwtToken);
//            // 댓글 소유자 확인
//            commentService.checkCommenter(commentId, userId);
//            // 댓글에 좋아요를 추가
//            commentService.likeComment(commentId, userId);
//            return ResponseEntity.ok("댓글에 좋아요를 추가");
//        }
//        catch (NoSuchElementException e)
//        {
//            return ResponseEntity.badRequest().body("댓글이 존재하지 않습니다");
//        }
//    }
//
//    // 댓글에 좋아요를 취소합니다.
//    @PutMapping("/api/question/{commentId}/unlike")
//    public ResponseEntity<String> unlikeComment(@PathVariable String commentId,
//                                                @RequestHeader("Authorization") String jwtToken) {
//
//        try
//        {
//            // JWT에서 사용자 ID 추출
//            Long userId = extractUserIdFromJwt(jwtToken);
//            // 댓글 소유자 확인
//            commentService.checkCommenter(commentId, userId);
//            // 댓글에 좋아요를 취소
//            commentService.unlikeComment(commentId, userId);
//            return ResponseEntity.ok("댓글에 좋아요를 취소");
//        }
//        catch (NoSuchElementException e)
//        {
//            return ResponseEntity.badRequest().body("댓글이 존재하지 않습니다");
//        }
//    }
//
//
//    // 댓글을 삭제합니다.
//    @DeleteMapping("/api/question/{commentId}/delete")
//    public void deleteComment(@PathVariable String commentId,
//                              @RequestHeader("Authorization") String jwtToken) {
//
//        try{
//            // JWT에서 사용자 ID 추출
//            Long userId = extractUserIdFromJwt(jwtToken);
//            // 댓글 소유자 확인
//            commentService.checkCommenter(commentId, userId);
//            // 댓글을 삭제
//            commentService.deleteComment(commentId);
//        }
//        catch (NoSuchElementException e)
//        {
//            return;
//        }
//    }

}
