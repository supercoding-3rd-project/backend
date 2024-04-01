package com.github.devsns.domain.qnas.controller;

import com.github.devsns.domain.qnas.dto.QuestionBoardReqDto;
import com.github.devsns.domain.qnas.service.QuestionBoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Slf4j
public class QuestionBoardController {

    private final QuestionBoardService questionBoardService;

    @PostMapping("/")
    public ResponseEntity<?> createQuestionBoard(@RequestBody QuestionBoardReqDto questionBoardReqDto) {
        questionBoardService.createQuestionBoard(questionBoardReqDto);
        return ResponseEntity.ok("질문이 등록되었습니다.");
    }
}
