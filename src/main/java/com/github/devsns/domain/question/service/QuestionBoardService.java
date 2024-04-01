package com.github.devsns.domain.question.service;

import com.github.devsns.domain.question.dto.QuestionBoardReqDto;
import com.github.devsns.domain.question.entity.LikeEntity;
import com.github.devsns.domain.question.entity.QuestionBoardEntity;
import com.github.devsns.domain.question.repository.LikeRepository;
import com.github.devsns.domain.question.repository.QuestionBoardRepository;
import com.github.devsns.domain.user.userEntities.UserEntity;
import com.github.devsns.domain.user.userRepository.UserRepository;
import com.github.devsns.exception.AppException;
import com.github.devsns.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class QuestionBoardService {

    private final QuestionBoardRepository questionBoardRepository;
    private final LikeRepository likeRepository;
    private final UserRepository userRepository;


    public void createQuestionBoard(QuestionBoardReqDto questionBoardReqDto) {
        UserEntity user = userRepository.findByEmail("email")
                .orElseThrow(() -> new AppException(ErrorCode.USE_EMAIL_NOT_FOUND.getMessage(), ErrorCode.USE_EMAIL_NOT_FOUND));

        QuestionBoardEntity questionBoard = QuestionBoardEntity.toEntity(user, questionBoardReqDto);

        questionBoardRepository.save(questionBoard);
    }

    public String questionBoardLike(Long questionBoardId) {
        QuestionBoardEntity questionBoard = questionBoardRepository.findById(questionBoardId).orElseThrow(
                () -> new AppException(ErrorCode.QUES_BOARD_NOT_FOUND.getMessage(), ErrorCode.QUES_BOARD_NOT_FOUND)
        );

        UserEntity user = userRepository.findByEmail("email").orElseThrow(
                () -> new AppException(ErrorCode.USE_EMAIL_NOT_FOUND.getMessage(), ErrorCode.USE_EMAIL_NOT_FOUND)
        );

        Optional<LikeEntity> likes = likeRepository.findByUserAndQuestionBoard(user, questionBoard);

        if(likes.isPresent()){
            likeRepository.delete(likes.get());
            return "좋아요를 취소했습니다.";
        } else {
            likeRepository.save(LikeEntity.toEntity(user, questionBoard));
            return "좋아요를 눌렀습니다.";
        }
    }
}
