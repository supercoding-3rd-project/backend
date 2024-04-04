package com.github.devsns.domain.question.controller;

import com.github.devsns.domain.question.dto.QuestionBoardReqDto;
import com.github.devsns.domain.question.dto.QuestionBoardResDto;
import com.github.devsns.domain.question.entity.QuestionBoardEntity;
import com.github.devsns.domain.question.service.QuestionBoardService;
import com.github.devsns.domain.userDetails.entity.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@Slf4j
public class QuestionBoardController {

    private final QuestionBoardService questionBoardService;

    @GetMapping
    public List<QuestionBoardEntity> findAllQuestionBoard() {
        return questionBoardService.findAllQuestionBoard();
    }

    @GetMapping("/{id}")
    public QuestionBoardResDto findByQuestionBoardId(@PathVariable Long id) {
        return questionBoardService.findQuestionBoardById(id);
    }

    @PostMapping("/create/question")
    public ResponseEntity<?> createQuestionBoard(@RequestBody QuestionBoardReqDto questionBoardReqDto, @AuthenticationPrincipal CustomUserDetails user) {
        questionBoardService.createQuestionBoard(questionBoardReqDto, user.getEmail());
        return ResponseEntity.ok("질문이 등록되었습니다.");
    }

    @PostMapping("/{quesId}/like")
    public ResponseEntity<?> likeQuestionBoard(@PathVariable Long quesId, @AuthenticationPrincipal CustomUserDetails user) {
        String result = questionBoardService.questionBoardLike(quesId, user.getEmail());
        return ResponseEntity.ok(result);
    }

    @GetMapping("/search")
    public List<QuestionBoardResDto> findQuesBoardByTitleKeyword(@RequestParam("keyword") String keyword) {
        return questionBoardService.findByNameContaining(keyword);
    }
}
