package com.github.devsns.domain.question.service;

import com.github.devsns.domain.question.dto.QuestionBoardReqDto;
import com.github.devsns.domain.question.entity.LikeEntity;
import com.github.devsns.domain.question.entity.QuestionBoardEntity;
import com.github.devsns.domain.question.repository.LikeRepository;
import com.github.devsns.domain.question.repository.QuestionBoardRepository;
import com.github.devsns.domain.user.entitiy.UserEntity;
import com.github.devsns.domain.user.repository.UserRepository;
import com.github.devsns.exception.AppException;
import com.github.devsns.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuestionBoardService {

    private final QuestionBoardRepository questionBoardRepository;
    private final LikeRepository likeRepository;
    private final UserRepository userRepository;

    public List<QuestionBoardEntity> findByNameContaining(String titleKeyword) {
        return questionBoardRepository.findQuestionBoardEntitiesByTitleContaining(titleKeyword).stream()
                .filter(questionBoard -> questionBoard.getDeletedAt().isBefore(LocalDateTime.now()))
                .collect(Collectors.toList());
    }

    // 질문 게시글 생성하기
    @Transactional
    public void createQuestionBoard(QuestionBoardReqDto questionBoardReqDto /*, String email*/) {
        UserEntity user = userRepository.findByEmail("user@email.com").orElseThrow(
                () -> new AppException(ErrorCode.USER_EMAIL_NOT_FOUND.getMessage(), ErrorCode.USER_EMAIL_NOT_FOUND)
        );

        QuestionBoardEntity questionBoard = QuestionBoardEntity.toEntity(/*user,*/ questionBoardReqDto);

        questionBoardRepository.save(questionBoard);
    }

    // 좋아요
    public String questionBoardLike(Long questionBoardId, String email) {
        QuestionBoardEntity questionBoard = questionBoardRepository.findById(questionBoardId).orElseThrow(
                () -> new AppException(ErrorCode.QUES_BOARD_NOT_FOUND.getMessage(), ErrorCode.QUES_BOARD_NOT_FOUND)
        );

        UserEntity user = userRepository.findByEmail(email).orElseThrow(
                () -> new AppException(ErrorCode.USER_EMAIL_NOT_FOUND.getMessage(), ErrorCode.USER_EMAIL_NOT_FOUND)
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
