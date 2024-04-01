package com.github.devsns.domain.qnas.controller;

import com.github.devsns.domain.qnas.service.QuestionBoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Slf4j
public class QuestionBoardController {

    private final QuestionBoardService questionBoardService;


}
