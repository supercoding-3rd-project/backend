package com.github.devsns.domain.question.service;

import com.github.devsns.domain.answers.entity.AnswerLike;
import com.github.devsns.domain.comments.dto.AnswerCommentResDto;
import com.github.devsns.domain.notifications.repository.NotificationRepository;
import com.github.devsns.domain.notifications.service.NotificationService;
import com.github.devsns.domain.question.dto.*;
import com.github.devsns.domain.question.entity.QuestionLike;
import com.github.devsns.domain.question.entity.QuestionBoardEntity;
import com.github.devsns.domain.question.entity.QuestionBoardStatusType;
import com.github.devsns.domain.question.repository.LikeRepository;
import com.github.devsns.domain.question.repository.QuestionBoardRepository;
import com.github.devsns.domain.user.entitiy.UserEntity;
import com.github.devsns.domain.user.repository.UserRepository;
import com.github.devsns.exception.AppException;
import com.github.devsns.exception.ErrorCode;
import com.github.devsns.global.constant.LikeType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class QuestionBoardService {

    private final QuestionBoardRepository questionBoardRepository;
    private final LikeRepository likeRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;

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

    @Transactional
    public LikeQuestionDto updateQuestionReaction(Long quesId, Long userId, LikeType likeType) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        QuestionBoardEntity questionBoard = questionBoardRepository.findById(quesId)
                .orElseThrow(() -> new IllegalArgumentException("질문을 찾을 수 없습니다."));
        Optional<QuestionLike> existingLike = likeRepository.findByQuestionBoardIdAndUserId(quesId, user);

        if (existingLike.isPresent()) {
            QuestionLike like = existingLike.get();
            if (like.getLikeType() == likeType) {
                likeRepository.delete(like);
                notificationService.deleteLikeQuestionNotification(questionBoard.getQuestioner().getUserId(), user.getUserId());
                log.info(likeType == LikeType.LIKE ? "좋아요가 취소되었습니다." : "싫어요가 취소되었습니다.");
                questionBoardRepository.flush(); // 변경 사항 즉시 반영
                questionBoard = questionBoardRepository.findById(quesId).orElseThrow(); // 새로고침
            } else {
                log.info("이미 다른 반응을 하셨습니다.");
            }
        } else {
            QuestionLike newLike = new QuestionLike();
            newLike.setUserId(user);
            newLike.setQuestionBoard(questionBoard);
            newLike.setLikeType(likeType);
            newLike.setCreatedAt(LocalDateTime.now());
            likeRepository.save(newLike);
            questionBoardRepository.flush(); // 변경 사항 즉시 반영
            questionBoard = questionBoardRepository.findById(quesId).orElseThrow(); // 새로고침
            if (likeType == LikeType.LIKE) {
                notificationService.sendLikeQuestionNotification(questionBoard.getQuestioner(), user, questionBoard);
                log.info("좋아요가 등록되었습니다.");
            } else {
                notificationService.sendDislikeQuestionNotification(questionBoard.getQuestioner(), user, questionBoard);
                log.info("싫어요가 등록되었습니다.");
            }
        }
        return getLikeQuestionDto(questionBoard, user);
    }

    @Transactional
    public LikeQuestionDto getLikeQuestionDto(QuestionBoardEntity questionBoard, UserEntity user) {
        long questionLikeCount = questionBoard.getLikes().stream()
                .filter(like -> like.getLikeType().equals(LikeType.LIKE))
                .count();
        long questionDislikeCount = questionBoard.getLikes().stream()
                .filter(like -> like.getLikeType().equals(LikeType.DISLIKE))
                .count();

        boolean isLiked = questionBoard.getLikes().stream()
                .anyMatch(like -> like.getUserId().getUserId().equals(user.getUserId()) && like.getLikeType().equals(LikeType.LIKE));
        boolean isDisliked = questionBoard.getLikes().stream()
                .anyMatch(like -> like.getUserId().getUserId().equals(user.getUserId()) && like.getLikeType().equals(LikeType.DISLIKE));

        return LikeQuestionDto.builder()
                .questionId(questionBoard.getId())
                .likeCount(questionLikeCount)
                .dislikeCount(questionDislikeCount)
                .isLiked(isLiked)
                .isDisliked(isDisliked)
                .build();
    }


    @Transactional
    public ReadQuestionDto getQuestionBoardDetails(Long quesId, Long userId) {
        QuestionBoardEntity questionBoard = questionBoardRepository.findById(quesId)
                .orElseThrow(() -> new AppException(ErrorCode.QUES_BOARD_NOT_FOUND.getMessage(), ErrorCode.QUES_BOARD_NOT_FOUND));

        long questionLikeCount = questionBoard.getLikes().stream()
                .filter(like -> like.getLikeType().equals(LikeType.LIKE))
                .count();
        long questionDislikeCount = questionBoard.getLikes().stream()
                .filter(like -> like.getLikeType().equals(LikeType.DISLIKE))
                .count();

        boolean isLiked = questionBoard.getLikes().stream()
                .anyMatch(like -> like.getUserId().getUserId().equals(userId) && like.getLikeType().equals(LikeType.LIKE));
        boolean isDisliked = questionBoard.getLikes().stream()
                .anyMatch(like -> like.getUserId().getUserId().equals(userId) && like.getLikeType().equals(LikeType.DISLIKE));

        List<ReadAnswerDto> answers = questionBoard.getAnswers().stream()
                .map(answer -> {
                    // 로그인한 사용자의 답변에 대한 좋아요 여부 조회
                    boolean answerIsLiked = answer.getLikes().stream()
                            .anyMatch(like -> like.getUserId().getUserId().equals(userId));

                    List<AnswerCommentResDto> answerComments = answer.getComments().stream()
                            .map(comment -> AnswerCommentResDto.builder()
                                    .id(comment.getId())
                                    .content(comment.getContent())
                                    .commenterId(comment.getCommenter().getUserId())
                                    .commenter(comment.getCommenter().getUsername())
                                    .createdAt(comment.getCreatedAt())
                                    .updatedAt(comment.getUpdatedAt())
                                    .build())
                            .collect(Collectors.toList());

                    return ReadAnswerDto.builder()
                            .answerId(answer.getId())
                            .content(answer.getContent())
                            .answerId(answer.getId())
                            .questionId(questionBoard.getId())
                            .answererId(answer.getAnswerer().getUserId())
                            .answerer(answer.getAnswerer().getUsername())
                            .createdAt(answer.getCreatedAt())
                            .updatedAt(answer.getUpdatedAt())
                            .likeCount(answer.getLikes().size())
                            .isLiked(answerIsLiked)
                            .answerComments(answerComments)
                            .build();
                })
                .collect(Collectors.toList());

        return ReadQuestionDto.builder()
                .questionId(questionBoard.getId())
                .title(questionBoard.getTitle())
                .content(questionBoard.getContent())
                .questionerId(questionBoard.getQuestioner().getUserId())
                .questioner(questionBoard.getQuestioner().getUsername())
                .createdAt(questionBoard.getCreatedAt())
                .updatedAt(questionBoard.getUpdatedAt())
                .isLiked(isLiked)
                .isDisliked(isDisliked)
                .likeCount(questionLikeCount)
                .dislikeCount(questionDislikeCount)
                .answers(answers)
                .build();
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

    private static final int pageSize = 5;


    // 전체 질문 게시판
    @Transactional
    public Map<Integer, List<QuestionBoardResDto>> findAllQuestionBoard() {
        List<QuestionBoardEntity> questionBoardEntities = questionBoardRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
        List<QuestionBoardEntity> submittedQuestions = questionBoardEntities.stream()
                .filter(questionBoard -> questionBoard.getStatusType().equals(QuestionBoardStatusType.SUBMIT))
                .collect(Collectors.toList());

        Map<Integer, List<QuestionBoardResDto>> result = new HashMap<>();
        int totalItems = submittedQuestions.size();
        int totalPages = (int) Math.ceil((double) totalItems / pageSize);

        for (int page = 0; page < totalPages; page++) {
            int fromIndex = page * pageSize;
            int toIndex = Math.min((page + 1) * pageSize, totalItems);
            List<QuestionBoardEntity> pageData = submittedQuestions.subList(fromIndex, toIndex);
            List<QuestionBoardResDto> dtoList = pageData.stream()
                    .map(QuestionBoardResDto::new)
                    .collect(Collectors.toList());
            result.put(page + 1, dtoList);
        }

        return result;
    }


    // 특정 id 질문 게시판
    @Transactional
    public Map<Integer, QuestionBoardResDto> findQuestionBoardById(Long questionBoardId) {
        QuestionBoardEntity questionBoard = questionBoardRepository.findById(questionBoardId)
                .filter(questionBoardEntity -> questionBoardEntity.getStatusType().equals(QuestionBoardStatusType.SUBMIT))
                .orElseThrow(
                        () -> new AppException(ErrorCode.QUES_BOARD_NOT_FOUND.getMessage(), ErrorCode.QUES_BOARD_NOT_FOUND)
                );

        Map<Integer, QuestionBoardResDto> result = new HashMap<>();
        result.put(1, new QuestionBoardResDto(questionBoard));
        return result;
    }


    @Transactional
    public Map<String, Map<Integer, List<Object>>> findByNameContaining(String keyword) {
        // 게시물 검색 결과 가져오기
        List<QuestionBoardEntity> matchedQuestionBoards = questionBoardRepository.findByTitleContainingOrContentContaining(keyword, keyword);
        log.info(matchedQuestionBoards.toString());

        Map<Integer, List<SearchQuestionDto>> boardResults = getPagedResults(matchedQuestionBoards, SearchQuestionDto::new, 5);
        log.info(boardResults.toString());

        // 유저 검색 결과 가져오기
        List<UserEntity> matchedUsers = userRepository.findByUsernameContaining(keyword);
        log.info(matchedUsers.toString());

        Map<Integer, List<SearchUserDto>> userResults = getPagedResults(matchedUsers, SearchUserDto::new, 5);
        log.info(userResults.toString());

        // 결과를 Map에 담아 반환
        Map<String, Map<Integer, List<Object>>> searchResult = new HashMap<>();
        searchResult.put("questionBoards", castToListObject(boardResults));
        searchResult.put("users", castToListObject(userResults));
        return searchResult;
    }

    private <T, R> Map<Integer, List<Object>> castToListObject(Map<Integer, List<R>> original) {
        return original.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> new ArrayList<>(entry.getValue())));
    }

    private <T, R> Map<Integer, List<R>> getPagedResults(List<T> items, Function<T, R> mapper, int pageSize) {
        Map<Integer, List<R>> pagedResults = new HashMap<>();
        int totalItems = items.size();
        int totalPages = (int) Math.ceil((double) totalItems / pageSize);

        for (int page = 0; page < totalPages; page++) {
            int fromIndex = page * pageSize;
            int toIndex = Math.min(fromIndex + pageSize, totalItems);
            List<R> pageItems = items.subList(fromIndex, toIndex).stream()
                    .map(mapper)
                    .collect(Collectors.toList());
            pagedResults.put(page + 1, pageItems);
        }

        return pagedResults;
    }
}
