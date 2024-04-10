package com.github.devsns.domain.answers.controller;

import com.github.devsns.domain.answers.dto.AnswerDto;
import com.github.devsns.domain.answers.service.AnswerService;
import com.github.devsns.global.component.ExtractIdUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@RestController
public class AnswerController {

    private final AnswerService answerService;
    private final ExtractIdUtil extractIdUtil;

    public AnswerController(AnswerService answerService, ExtractIdUtil extractIdUtil) {
        this.extractIdUtil = extractIdUtil;
        this.answerService = answerService;
    }


    @PostMapping("/api/{quesId}/answer/create")
    public ResponseEntity<String> createAnswer(@PathVariable Long quesId,
                                               @RequestBody AnswerDto answerDto,
                                               Authentication authentication) {

        // Authentication 객체에서 사용자 ID 추출
        Long userId = extractIdUtil.extractUserIdFromAuthentication(authentication);

        String title = answerDto.getTitle();
        String content = answerDto.getContent();

        // AnswerService의 createAnswer() 메서드 호출
        answerService.createAnswer(quesId, userId, title, content);

        return ResponseEntity.ok("답변 작성 완료");
    }

    @PostMapping("/api/answer/{answerId}/like")
    public ResponseEntity<String> likeAnswer(@PathVariable Long answerId,
                                             Authentication authentication) {
        try {
            // Authentication 객체에서 사용자 ID 추출
            Long userId = extractIdUtil.extractUserIdFromAuthentication(authentication);
            // 답변 소유자 확인
            answerService.checkAnswerer(answerId, userId);
            // 좋아요 생성
            answerService.likeAnswer(answerId, userId);
        } catch (NoSuchElementException e) {
            return ResponseEntity.badRequest().body("답변이 존재하지 않습니다");
        }
        return ResponseEntity.ok("좋아요 성공");
    }


    @PostMapping("/api/answer/{answerId}/unlike")
    public ResponseEntity<String> unlikeAnswer(@PathVariable Long answerId,
                                               Authentication authentication) {
        try {
            // Authentication 객체에서 사용자 ID 추출
            Long userId = extractIdUtil.extractUserIdFromAuthentication(authentication);
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
                                               Authentication authentication) {
        try {
            // Authentication 객체에서 사용자 ID 추출
            Long userId = extractIdUtil.extractUserIdFromAuthentication(authentication);
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
                                               @RequestBody AnswerDto answerDto,
                                               Authentication authentication) {

        String title = answerDto.getTitle();
        String content = answerDto.getContent();

        try {
            // Authentication 객체에서 사용자 ID 추출
            Long userId = extractIdUtil.extractUserIdFromAuthentication(authentication);
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
