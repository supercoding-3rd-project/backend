package com.github.devsns.domain.question.controller;

import com.github.devsns.domain.question.dto.QuestionBoardReqDto;
import com.github.devsns.domain.question.service.QuestionBoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Slf4j
public class QuestionBoardController {

    private final QuestionBoardService questionBoardService;

    @PostMapping("/createQuestion")
    public ResponseEntity<?> createQuestionBoard(@RequestBody QuestionBoardReqDto questionBoardReqDto) {
        questionBoardService.createQuestionBoard(questionBoardReqDto);
        return ResponseEntity.ok("질문이 등록되었습니다.");
    }

    @PostMapping("/{quesId}/like")
    public ResponseEntity<String> likeQuestionBoard(@PathVariable Long quesId, Principal principal) {
        String result = questionBoardService.questionBoardLike(quesId);
        return ResponseEntity.ok(result);
    }
}
