package com.github.devsns.domain.comments.controller;

import com.github.devsns.domain.comments.dto.CommentRequest;
import com.github.devsns.domain.comments.service.CommentService;
import com.nimbusds.jose.shaded.gson.JsonObject;
import com.nimbusds.jose.shaded.gson.JsonParser;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;

@RestController
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    // JWT에서 사용자 ID 추출하는 메서드
    private Long extractUserIdFromJwt(String jwtToken) {
        // JWT 해독 로직 구현
        // 예시로 간단하게 추출하는 것으로 가정
        // 실제 구현에서는 JWT 라이브러리를 사용하여 JWT를 해독해야 함
        // 여기에서는 사용자 ID가 JWT의 일부로 들어 있다고 가정
        String[] parts = jwtToken.split("\\.");
        String payload = new String(Base64.getDecoder().decode(parts[1]));
        JsonObject jsonObject = JsonParser.parseString(payload).getAsJsonObject();
        return jsonObject.get("userId").getAsLong();
    }

    // 특정 포스트에 댓글을 생성합니다.
    @PostMapping("/api/posts/{postId}/comments")
    public ResponseEntity<String> createComment(@PathVariable Long postId,
                              @RequestBody CommentRequest commentRequest,
                              @RequestHeader("Authorization") String jwtToken) {

// JWT에서 사용자 ID 추출
        Long userId = extractUserIdFromJwt(jwtToken);

        String content = commentRequest.getContent(); // 댓글 내용

        // CommentService의 createComment 메서드 호출
        commentService.createComment(postId, userId, content);

        return ResponseEntity.ok("댓글 작성 완료");
    }

    // 댓글을 업데이트합니다.
    @PutMapping("/api/comments/{commentId}")
    public void updateComment(@PathVariable String commentId) {
        // 구현
    }

    // 댓글에 좋아요를 추가합니다.
    @PostMapping("/api/comments/{commentId}/like")
    public void likeComment(@PathVariable String commentId) {
        // 구현
    }

    // 댓글에 좋아요를 취소합니다.
    @PostMapping("/api/comments/{commentId}/unlike")
    public void unlikeComment(@PathVariable String commentId) {
        // 구현
    }

    // 댓글에 대댓글을 생성합니다.
    @PostMapping("/api/comments/{commentId}/reply")
    public void replyComment(@PathVariable String commentId) {
        // 구현
    }

    // 댓글을 삭제합니다.
    @DeleteMapping("/api/comments/{commentId}/delete")
    public void deleteComment(@PathVariable String commentId) {
        // 구현
    }
}
