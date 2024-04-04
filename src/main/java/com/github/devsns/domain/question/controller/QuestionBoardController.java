package com.github.devsns.domain.question.controller;

import com.github.devsns.domain.auth.entity.CustomUserDetails;
import com.github.devsns.domain.question.dto.QuestionBoardReqDto;
import com.github.devsns.domain.question.dto.QuestionBoardResDto;
import com.github.devsns.domain.question.service.QuestionBoardService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Api(tags = "Some API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Slf4j
public class QuestionBoardController {

    private final QuestionBoardService questionBoardService;

    @GetMapping("/v1")
    public List<QuestionBoardResDto> findAllQuestionBoard() {
        return questionBoardService.findAllQuestionBoard();
    }

    @GetMapping("/v1/{id}")
    public QuestionBoardResDto findByQuestionBoardId(@PathVariable Long id) {
        return questionBoardService.findQuestionBoardById(id);
    }

    @PostMapping("/create/question")
    public ResponseEntity<?> createQuestionBoard(@RequestBody QuestionBoardReqDto questionBoardReqDto, @AuthenticationPrincipal CustomUserDetails user) {
        String result = questionBoardService.createQuestionBoard(questionBoardReqDto, user.getUsername());
        return ResponseEntity.ok(result);
    }

    @PostMapping("/{quesId}/like")
    public ResponseEntity<?> likeQuestionBoard(@PathVariable Long quesId, @AuthenticationPrincipal CustomUserDetails user) {
        String result = questionBoardService.questionBoardLike(quesId, user.getUsername());
        return ResponseEntity.ok(result);
    }

    @GetMapping("/v1/search")
    public List<QuestionBoardResDto> findQuesBoardByTitleKeyword(@RequestParam("keyword") String keyword) {
        return questionBoardService.findByNameContaining(keyword);
    }

    @DeleteMapping("/delete/{questionId}")
    public ResponseEntity<?> deleteQuestion(@PathVariable Long questionId) {
        questionBoardService.deleteQuestionBoard(questionId);
        return ResponseEntity.ok("질문이 삭제되었습니다.");
    }
}
