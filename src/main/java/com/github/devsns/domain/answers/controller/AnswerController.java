package com.github.devsns.domain.answers.controller;

import com.github.devsns.domain.answers.dto.AnswerReqDto;
import com.github.devsns.domain.answers.service.AnswerService;
import com.github.devsns.domain.question.dto.LikeAnswerDto;
import com.github.devsns.domain.question.dto.ReadAnswerDto;
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
@Tag(name = "답변 API", description = "답변 C, U, D 및 좋아요/싫어요 C,D")
public class AnswerController {

    private final AnswerService answerService;
    private final ExtractUserDataUtil extractUserDataUtil;

    @Operation(summary = "질문(질문 ID)에 답변을 생성")
    @PostMapping("/v1/question/{quesId}/answer/create")
    public ResponseEntity<String> createAnswer(@PathVariable Long quesId,
                                               @RequestBody AnswerReqDto answerReqDto,
                                               Authentication authentication) {

        // Authentication 객체에서 사용자 ID 추출
        Long userId = extractUserDataUtil.extractUserIdFromAuthentication(authentication);

        String content = answerReqDto.getContent();
        String userName = extractUserDataUtil.extractUserNameFromAuthentication(authentication);


        // AnswerService의 createAnswer() 메서드 호출
        answerService.createAnswer(quesId, userId, userName, content);

        return ResponseEntity.ok("답변 작성 완료");
    }

    @Operation(summary = "답변에 좋아요 및 좋아요 취소 / 최초 좋아요면 저장, 이미 좋아요 내역 있으면 취소")
    @PatchMapping("/v1/answer/{answerId}/like")
    public ResponseEntity<?> toggleLikeAnswer(@PathVariable Long answerId,
                                              Authentication authentication) {
        try {
            // Authentication 객체에서 사용자 ID 추출
            Long userId = extractUserDataUtil.extractUserIdFromAuthentication(authentication);
            // 답변 소유자 확인
            answerService.checkAnswerer(answerId, userId);
            // 좋아요 확인
            LikeAnswerDto answer = answerService.updateAnswerReaction(answerId, userId);
            return ResponseEntity.ok(answer);
        } catch (NoSuchElementException e) {
            return ResponseEntity.badRequest().body("답변이 존재하지 않습니다");
        }
    }


    @Operation(summary = "답변 삭제 기능 => 답변 삭제 시 모든 연관 요소 삭제")
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
    @Operation(summary = "답변 삭제 기능")
    @PutMapping("/v1/answer/{answerId}/update")
    public ResponseEntity<String> updateAnswer(@PathVariable Long answerId,
                                               @RequestBody AnswerReqDto answerReqDto,
                                               Authentication authentication) {

        String content = answerReqDto.getContent();

        try {
            // Authentication 객체에서 사용자 ID 추출
            Long userId = extractUserDataUtil.extractUserIdFromAuthentication(authentication);
            // 답변 소유자 확인
            answerService.checkAnswerer(answerId, userId);
            // 답변 업데이트
            answerService.updateAnswer(answerId, userId, content);
            return ResponseEntity.ok("답변 업데이트");

        } catch (NoSuchElementException e) {
            return ResponseEntity.badRequest().body("답변이 존재하지 않습니다");
        }
    }

}
