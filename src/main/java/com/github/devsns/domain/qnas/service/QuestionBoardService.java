package com.github.devsns.domain.qnas.service;

import com.github.devsns.domain.qnas.dto.QuestionBoardReqDto;
import com.github.devsns.domain.qnas.repository.LikeRepository;
import com.github.devsns.domain.qnas.repository.QuestionBoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuestionBoardService {

    private final QuestionBoardRepository questionBoardRepository;
    private final LikeRepository likeRepository;


    public void createQuestionBoard(QuestionBoardReqDto questionBoardReqDto) {
    }
}
