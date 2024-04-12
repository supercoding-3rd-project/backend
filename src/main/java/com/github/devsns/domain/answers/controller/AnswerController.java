package com.github.devsns.domain.answers.controller;

import com.github.devsns.domain.answers.dto.AnswerReqDto;
import com.github.devsns.domain.answers.service.AnswerService;
import com.github.devsns.global.component.ExtractUserDataUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AnswerController {

    private final AnswerService answerService;
    private final ExtractUserDataUtil extractUserDataUtil;


    @PostMapping("/v1/question/{quesId}/answer/create")
    public ResponseEntity<String> createAnswer(@PathVariable Long quesId,
                                               @RequestBody AnswerReqDto answerReqDto,
                                               Authentication authentication) {

        // Authentication 객체에서 사용자 ID 추출
        Long userId = extractUserDataUtil.extractUserIdFromAuthentication(authentication);

        String title = answerReqDto.getTitle();
        String content = answerReqDto.getContent();


        // AnswerService의 createAnswer() 메서드 호출
        answerService.createAnswer(quesId, userId, title, content);

        return ResponseEntity.ok("답변 작성 완료");
    }

    @PostMapping("/v1/answer/{answerId}/like")
    public ResponseEntity<String> likeAnswer(@PathVariable Long answerId,
                                             Authentication authentication) {
        try {
            // Authentication 객체에서 사용자 ID 추출
            Long userId = extractUserDataUtil.extractUserIdFromAuthentication(authentication);
            // 답변 소유자 확인
            answerService.checkAnswerer(answerId, userId);
            // 좋아요 확인
            String message = answerService.likeAnswer(answerId, userId);
            return ResponseEntity.ok(message);
        } catch (NoSuchElementException e) {
            return ResponseEntity.badRequest().body("답변이 존재하지 않습니다");
        }

    }


    @DeleteMapping("/v1/answer/{answerId}/delete")
    public ResponseEntity<String> deleteAnswer(@PathVariable Long answerId,
                                               Authentication authentication) {
        try {
            // Authentication 객체에서 사용자 ID 추출
            Long userId = extractUserDataUtil.extractUserIdFromAuthentication(authentication);
            // 답변 소유자 확인
            answerService.checkAnswerer(answerId, userId);
            // 답변 삭제
            answerService.deleteAnswer(answerId);
            return ResponseEntity.ok("답변 삭제");
        } catch (NoSuchElementException e) {
            return ResponseEntity.badRequest().body("답변이 존재하지 않습니다");
        }
    }

    @PutMapping("/v1/answer/{answerId}/update")
    public ResponseEntity<String> updateAnswer(@PathVariable Long answerId,
                                               @RequestBody AnswerReqDto answerReqDto,
                                               Authentication authentication) {

        String title = answerReqDto.getTitle();
        String content = answerReqDto.getContent();

        try {
            // Authentication 객체에서 사용자 ID 추출
            Long userId = extractUserDataUtil.extractUserIdFromAuthentication(authentication);
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
