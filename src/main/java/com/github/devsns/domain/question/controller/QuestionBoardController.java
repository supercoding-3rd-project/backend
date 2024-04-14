package com.github.devsns.domain.question.controller;

import com.github.devsns.domain.auth.entity.CustomUserDetails;
import com.github.devsns.domain.question.dto.QuestionBoardReqDto;
import com.github.devsns.domain.question.dto.QuestionBoardResDto;
import com.github.devsns.domain.question.dto.ReadQuestionDto;
import com.github.devsns.domain.question.service.QuestionBoardService;
import com.github.devsns.domain.user.entitiy.UserEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Slf4j
@Tag(name = "질문 및 검색 API", description = "질문 C, R, D 및 좋아요 C,D, 유저 ID / 키워드 검색")
public class QuestionBoardController {

    private final QuestionBoardService questionBoardService;

    @GetMapping("/search")
    @Operation(summary = "메인 화면 전체 게시물 출력")
    public Map<Integer, List<QuestionBoardResDto>> findAllQuestionBoard() {
        return questionBoardService.findAllQuestionBoard();
    }

    @GetMapping("/search/user/{id}")
    @Operation(summary = "USER ID 통해서 작성한 게시물 출력")
    public Map<Integer, QuestionBoardResDto> findByQuestionBoardId(@PathVariable Long id) {
        return questionBoardService.findQuestionBoardById(id);
    }

    @GetMapping("/search/keyword")
    @Operation(summary = "KEYWORD 통해서 질문 게시물(제목, 내용) / 유저 이름 검색 후 Map<K, V>로 보냄")
    public Map<String, Map<Integer, List<Object>>> findQuesBoardByTitleKeyword(@RequestParam("keyword") String keyword) {
        return questionBoardService.findByNameContaining(keyword);
    }

    @PostMapping("v1/question/create")
    @Operation(summary = "질문 생성")
    public ResponseEntity<?> createQuestionBoard(@RequestBody QuestionBoardReqDto questionBoardReqDto, @AuthenticationPrincipal CustomUserDetails user) {
        String result = questionBoardService.createQuestionBoard(questionBoardReqDto, user.getUsername());
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("v1/question/delete/{quesId}")
    @Operation(summary = "질문 삭제 => 질문 삭제 시 모든 연관 요소 삭제")
    public ResponseEntity<?> deleteQuestion(@PathVariable Long quesId) {
        questionBoardService.deleteQuestionBoard(quesId);
        return ResponseEntity.ok("질문이 삭제되었습니다.");
    }

    @GetMapping("/question/{quesId}")
    @Operation(summary = "하나의 질문 조회")
    public ResponseEntity<?> readQuestionBoard(@PathVariable Long quesId, @AuthenticationPrincipal UserDetails userDetails) {
        Long userId = null;
        if (userDetails instanceof CustomUserDetails) {
            userId = ((CustomUserDetails) userDetails).getUserEntity().getUserId();
        }
        ReadQuestionDto readQuestionDto = questionBoardService.getQuestionBoardDetails(quesId, userId);
        return ResponseEntity.ok(readQuestionDto);
    }


    @PostMapping("v1/question/{quesId}/like")
    @Operation(summary = "질문에 대한 좋아요 / 좋아요 취소")
    public ResponseEntity<?> likeQuestionBoard(@PathVariable Long quesId, @AuthenticationPrincipal CustomUserDetails user) {
        String result = questionBoardService.questionBoardLike(quesId, user.getUsername());
        return ResponseEntity.ok(result);
    }





}
