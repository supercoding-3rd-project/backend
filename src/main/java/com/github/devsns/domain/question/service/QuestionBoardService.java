package com.github.devsns.domain.question.service;

import com.github.devsns.domain.question.dto.QuestionBoardReqDto;
import com.github.devsns.domain.question.repository.LikeRepository;
import com.github.devsns.domain.question.repository.QuestionBoardRepository;
import com.github.devsns.domain.user.userEntities.UserEntity;
import com.github.devsns.domain.user.userRepository.UserRepository;
import com.github.devsns.exception.AppException;
import com.github.devsns.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuestionBoardService {

    private final QuestionBoardRepository questionBoardRepository;
    private final LikeRepository likeRepository;
    private final UserRepository userRepository;


    public void createQuestionBoard(QuestionBoardReqDto questionBoardReqDto) {
//        UserEntity user = userRepository.findByEmail(email)
//                .orElseThrow(() -> new AppException(ErrorCode.USE_EMAIL_NOT_FOUND.getMessage(), ErrorCode.USE_EMAIL_NOT_FOUND));
    }
}
