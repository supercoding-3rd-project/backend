package com.github.devsns.domain.question.service;

import com.github.devsns.domain.question.dto.QuestionBoardReqDto;
import com.github.devsns.domain.question.dto.QuestionBoardResDto;
import com.github.devsns.domain.question.entity.LikeEntity;
import com.github.devsns.domain.question.entity.QuestionBoardEntity;
import com.github.devsns.domain.question.entity.QuestionBoardStatusType;
import com.github.devsns.domain.question.repository.LikeRepository;
import com.github.devsns.domain.question.repository.QuestionBoardRepository;
import com.github.devsns.domain.user.entitiy.UserEntity;
import com.github.devsns.domain.user.repository.UserRepository;
import com.github.devsns.exception.AppException;
import com.github.devsns.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuestionBoardService {

    private final QuestionBoardRepository questionBoardRepository;
    private final LikeRepository likeRepository;
    private final UserRepository userRepository;

    // 검색
    public List<QuestionBoardResDto> findByNameContaining(String titleKeyword) {
        List<QuestionBoardEntity> questionBoardEntities = questionBoardRepository.findQuestionBoardEntitiesByTitleContaining(titleKeyword);

        return questionBoardEntities.stream()
                .filter(questionBoardEntity -> questionBoardEntity.getStatusType().equals(QuestionBoardStatusType.SUBMIT))
                .map(
                        (questionBoard) -> new QuestionBoardResDto(questionBoard, likeRepository.countByQuestionBoard(questionBoard))
                )
                .collect(Collectors.toList());
    }

    // 질문 게시글 생성하기
    @Transactional
    public String createQuestionBoard(QuestionBoardReqDto questionBoardReqDto, String email) {
        UserEntity user = userRepository.findByEmail(email).orElseThrow(
                () -> new AppException(ErrorCode.USER_EMAIL_NOT_FOUND.getMessage(), ErrorCode.USER_EMAIL_NOT_FOUND)
        );

        String status = questionBoardReqDto.getStatusType();

        QuestionBoardEntity questionBoard = QuestionBoardEntity.toEntity(user, questionBoardReqDto);

        questionBoardRepository.save(questionBoard);

        if (status.equals(QuestionBoardStatusType.SUBMIT.getStatus())) return "질문이 등록되었습니다.";
        else return "임시 저장되었습니다.";
    }

    // 좋아요
    public String questionBoardLike(Long questionBoardId, String email) {
        QuestionBoardEntity questionBoard = questionBoardRepository.findById(questionBoardId)
                .filter(questionBoardEntity -> questionBoardEntity.getStatusType().equals(QuestionBoardStatusType.SUBMIT))
                .orElseThrow(
                () -> new AppException(ErrorCode.QUES_BOARD_NOT_FOUND.getMessage(), ErrorCode.QUES_BOARD_NOT_FOUND));

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

    // 전체 질문 게시판
    public List<QuestionBoardResDto> findAllQuestionBoard() {
        List<QuestionBoardEntity> questionBoardEntities = questionBoardRepository.findAll();

        return questionBoardEntities.stream()
                .filter(
                        questionBoard -> questionBoard.getStatusType().equals(QuestionBoardStatusType.SUBMIT)
                )
                .map(
                        questionBoard -> new QuestionBoardResDto(questionBoard, likeRepository.countByQuestionBoard(questionBoard))
                )
                .collect(Collectors.toList());
    }

    // 특정 id 질문 게시판
    public QuestionBoardResDto findQuestionBoardById(Long questionBoardId) {
        QuestionBoardEntity questionBoard = questionBoardRepository.findById(questionBoardId)
                .filter(questionBoardEntity -> questionBoardEntity.getStatusType().equals(QuestionBoardStatusType.SUBMIT))
                .orElseThrow(
                () -> new AppException(ErrorCode.QUES_BOARD_NOT_FOUND.getMessage(), ErrorCode.QUES_BOARD_NOT_FOUND));

        Long likeCount = likeRepository.countByQuestionBoard(questionBoard);

        return new QuestionBoardResDto(questionBoard, likeCount);
    }

    // 삭제
    @Transactional
    public void deleteQuestionBoard(Long questionId) {
        QuestionBoardEntity questionBoard = questionBoardRepository.findById(questionId)
                .filter(questionBoardEntity -> questionBoardEntity.getStatusType().equals(QuestionBoardStatusType.SUBMIT))
                .orElseThrow(
                        () -> new AppException(ErrorCode.QUES_BOARD_NOT_FOUND.getMessage(), ErrorCode.QUES_BOARD_NOT_FOUND));

        questionBoardRepository.delete(questionBoard);
    }
}
