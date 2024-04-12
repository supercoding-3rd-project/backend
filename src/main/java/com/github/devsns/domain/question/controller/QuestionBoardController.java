package com.github.devsns.domain.question.controller;

import com.github.devsns.domain.auth.entity.CustomUserDetails;
import com.github.devsns.domain.question.dto.QuestionBoardReqDto;
import com.github.devsns.domain.question.dto.QuestionBoardResDto;
import com.github.devsns.domain.question.service.QuestionBoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Slf4j
public class QuestionBoardController {

    private final QuestionBoardService questionBoardService;

    @GetMapping("/search")
    public Map<Integer, List<QuestionBoardResDto>> findAllQuestionBoard() {
        return questionBoardService.findAllQuestionBoard();
    }

    @GetMapping("/search/user/{id}")
    public Map<Integer, QuestionBoardResDto> findByQuestionBoardId(@PathVariable Long id) {
        return questionBoardService.findQuestionBoardById(id);
    }

    @GetMapping("/search/keyword")
    public Map<Integer, List<QuestionBoardResDto>> findQuesBoardByTitleKeyword(@RequestParam("keyword") String keyword) {
        return questionBoardService.findByNameContaining(keyword);
    }

    @PostMapping("v1/question/create")
    public ResponseEntity<?> createQuestionBoard(@RequestBody QuestionBoardReqDto questionBoardReqDto, @AuthenticationPrincipal CustomUserDetails user) {
        String result = questionBoardService.createQuestionBoard(questionBoardReqDto, user.getUsername());
        return ResponseEntity.ok(result);
    }

    @PostMapping("v1/question/{quesId}/like")
    public ResponseEntity<?> likeQuestionBoard(@PathVariable Long quesId, @AuthenticationPrincipal CustomUserDetails user) {
        String result = questionBoardService.questionBoardLike(quesId, user.getUsername());
        return ResponseEntity.ok(result);
    }


    @DeleteMapping("v1/question/delete/{quesId}")
    public ResponseEntity<?> deleteQuestion(@PathVariable Long quesId) {
        questionBoardService.deleteQuestionBoard(quesId);
        return ResponseEntity.ok("질문이 삭제되었습니다.");
    }
}
