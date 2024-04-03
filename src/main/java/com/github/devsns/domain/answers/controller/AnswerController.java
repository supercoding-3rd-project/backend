package com.github.devsns.domain.answers.controller;

import com.github.devsns.domain.answers.dto.AnswerRequest;
import com.github.devsns.domain.answers.service.AnswerService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@RestController
public class AnswerController {

    private final AnswerService answerService;

    public AnswerController(AnswerService answerService) {
        this.answerService = answerService;
    }

    // JWT에서 사용자 ID 추출하는 메서드
    public Long extractUserIdFromJwt(String jwtToken) {
        // 토큰 발급 시 사용한 암호화 키
        String secretKey = "your-secret-key";

        // JWT 토큰에서 payload 추출
        Claims claims = Jwts.parser()
                .setSigningKey(secretKey.getBytes())
                .parseClaimsJws(jwtToken)
                .getBody();

        // payload에서 사용자 ID 추출
        return claims.get("userId", Long.class);
    }

    @PostMapping("/api/answer/{quesId}/create")
    public ResponseEntity<String> createAnswer(@PathVariable Long quesId,
                                               @RequestBody AnswerRequest answerRequest,
                                               @RequestHeader("Authorization") String jwtToken) {

        // JWT에서 사용자 ID 추출
        Long userId = extractUserIdFromJwt(jwtToken);

        String title = answerRequest.getTitle();
        String content = answerRequest.getContent();

        // AnswerService의 createAnswer() 메서드 호출
        answerService.createAnswer(quesId, userId, title, content);

        return ResponseEntity.ok("답변 작성 완료");

    }

    @PostMapping("/api/answer/{answerId}/like")
    public ResponseEntity<String> likeAnswer(@PathVariable Long answerId,
                                             @RequestHeader("Authorization") String jwtToken) {
        try {
            // JWT에서 사용자 ID 추출
            Long userId = extractUserIdFromJwt(jwtToken);
            // 답변 소유자 확인
            answerService.checkAnswerer(answerId, userId);
            // 답변에 좋아요를 추가
            answerService.likeAnswer(answerId, userId);
            return ResponseEntity.ok("답변에 좋아요를 추가");
        } catch (NoSuchElementException e) {
            return ResponseEntity.badRequest().body("답변이 존재하지 않습니다");
        }
    }

    @PostMapping("/api/answer/{answerId}/unlike")
    public ResponseEntity<String> unlikeAnswer(@PathVariable Long answerId,
                                               @RequestHeader("Authorization") String jwtToken) {
        try {
            // JWT에서 사용자 ID 추출
            Long userId = extractUserIdFromJwt(jwtToken);
            // 답변 소유자 확인
            answerService.checkAnswerer(answerId, userId);
            // 답변에 좋아요를 취소
            answerService.unlikeAnswer(answerId, userId);
            return ResponseEntity.ok("답변에 좋아요를 취소");
        } catch (NoSuchElementException e) {
            return ResponseEntity.badRequest().body("답변이 존재하지 않습니다");
        }
    }

    @DeleteMapping("/api/answer/{answerId}/delete")
    public ResponseEntity<String> deleteAnswer(@PathVariable Long answerId,
                                               @RequestHeader("Authorization") String jwtToken) {
        try {
            // JWT에서 사용자 ID 추출
            Long userId = extractUserIdFromJwt(jwtToken);
            // 답변 소유자 확인
            answerService.checkAnswerer(answerId, userId);
            // 답변 삭제
            answerService.deleteAnswer(answerId);
            return ResponseEntity.ok("답변 삭제");
        } catch (NoSuchElementException e) {
            return ResponseEntity.badRequest().body("답변이 존재하지 않습니다");
        }
    }

    @PutMapping("/api/answer/{answerId}/update")
    public ResponseEntity<String> updateAnswer(@PathVariable Long answerId,
                                               @RequestBody AnswerRequest answerRequest,
                                               @RequestHeader("Authorization") String jwtToken) {

        String title = answerRequest.getTitle();
        String content = answerRequest.getContent();

        try {
            // JWT에서 사용자 ID 추출
            Long userId = extractUserIdFromJwt(jwtToken);
            // 답변 소유자 확인
            answerService.checkAnswerer(answerId, userId);
            // 답변 업데이트
            answerService.updateAnswer(answerId, userId, title, content);
            return ResponseEntity.ok("답변 업데이트");

        } catch (NoSuchElementException e) {
            return ResponseEntity.badRequest().body("답변이 존재하지 않습니다");
        }
    }

}
