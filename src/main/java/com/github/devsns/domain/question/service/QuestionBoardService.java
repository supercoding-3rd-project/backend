package com.github.devsns.domain.question.service;

import com.github.devsns.domain.comments.dto.ReadAnswerCommentResDto;
import com.github.devsns.domain.notifications.service.NotificationService;
import com.github.devsns.domain.question.dto.*;
import com.github.devsns.domain.question.dto.like.LikeQuestionDto;
import com.github.devsns.domain.question.dto.read.ReadAnswerDto;
import com.github.devsns.domain.question.dto.read.ReadQuestionDto;
import com.github.devsns.domain.question.dto.search.MainAllData;
import com.github.devsns.domain.question.dto.search.SearchData;
import com.github.devsns.domain.question.dto.search.SearchQuestionDto;
import com.github.devsns.domain.question.dto.search.SearchUserDto;
import com.github.devsns.domain.question.entity.QuestionLike;
import com.github.devsns.domain.question.entity.QuestionBoardEntity;
import com.github.devsns.domain.question.entity.TempQuestionEntity;
import com.github.devsns.domain.question.repository.LikeRepository;
import com.github.devsns.domain.question.repository.QuestionBoardRepository;
import com.github.devsns.domain.question.repository.TempQuestionRepository;
import com.github.devsns.domain.user.entitiy.UserEntity;
import com.github.devsns.domain.user.repository.UserRepository;
import com.github.devsns.exception.AppException;
import com.github.devsns.exception.ErrorCode;
import com.github.devsns.global.constant.LikeType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    private final TempQuestionRepository tempQuestionRepository;



    @Transactional(readOnly = true)
    public List<TempQuestionResDto> getTemporarySavedQuestions(String email) {
        UserEntity user = userRepository.findByEmail(email).orElseThrow(
                () -> new AppException(ErrorCode.USER_EMAIL_NOT_FOUND.getMessage(), ErrorCode.USER_EMAIL_NOT_FOUND)
        );

        List<TempQuestionEntity> tempQuestions = tempQuestionRepository.findByQuestioner(user);
        if (tempQuestions.isEmpty()) {
            return Collections.emptyList();
        } else {
            return tempQuestions.stream()
                    .map(TempQuestionResDto::from)
                    .collect(Collectors.toList());
        }
    }

    @Transactional
    public QuestionCreateDto submitQuestion(QuestionBoardReqDto questionBoardReqDto, String email) {
        UserEntity user = userRepository.findByEmail(email).orElseThrow(
                () -> new AppException(ErrorCode.USER_EMAIL_NOT_FOUND.getMessage(), ErrorCode.USER_EMAIL_NOT_FOUND)
        );

        QuestionBoardEntity questionBoard = QuestionBoardEntity.toEntity(user, questionBoardReqDto);
        questionBoardRepository.save(questionBoard);

        // 사용자의 이메일을 이용하여 최근에 작성한 글을 조회
        List<QuestionBoardEntity> recentQuestions = questionBoardRepository.findAllByQuestionerOrderByCreatedAtDesc(user);

        // 최근에 작성한 글 중에서 가장 최근에 작성된 글의 ID 가져오기
        Long questionId = recentQuestions.get(0).getId();

        return new QuestionCreateDto("질문이 등록되었습니다.", questionId);
    }

    @Transactional
    public List<TempQuestionResDto> saveTemporaryQuestion(TempQuestionReqDto tempQuestionReqDto, String email) {
        UserEntity user = userRepository.findByEmail(email).orElseThrow(
                () -> new AppException(ErrorCode.USER_EMAIL_NOT_FOUND.getMessage(), ErrorCode.USER_EMAIL_NOT_FOUND)
        );

        TempQuestionEntity tempQuestion = new TempQuestionEntity();
        tempQuestion.setTitle(tempQuestionReqDto.getTitle());
        tempQuestion.setContent(tempQuestionReqDto.getContent());
        tempQuestion.setCreatedAt(LocalDateTime.now());
        tempQuestion.setQuestioner(user);
        tempQuestionRepository.save(tempQuestion);

        // 사용자의 모든 임시 저장된 질문을 조회
        List<TempQuestionEntity> tempQuestions = tempQuestionRepository.findByQuestioner(user);
        return tempQuestions.stream()
                .map(TempQuestionResDto::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<TempQuestionResDto> deleteTemporaryQuestion(Long tempId, String email) {
        UserEntity user = userRepository.findByEmail(email).orElseThrow(
                () -> new AppException(ErrorCode.USER_EMAIL_NOT_FOUND.getMessage(), ErrorCode.USER_EMAIL_NOT_FOUND)
        );

        tempQuestionRepository.deleteByQuestionerAndId(user, tempId);

        // 삭제 후 남은 임시 저장 목록 조회
        List<TempQuestionEntity> remainingTempQuestions = tempQuestionRepository.findByQuestioner(user);
        return remainingTempQuestions.stream()
                .map(TempQuestionResDto::from)
                .collect(Collectors.toList());
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


        boolean canDeleteQuestion = userId != null && questionBoard.getQuestioner().getUserId().equals(userId);

        List<ReadAnswerDto> answers = questionBoard.getAnswers().stream()
                .map(answer -> {
                    // 로그인한 사용자의 답변에 대한 좋아요 여부 조회
                    boolean answerIsLiked = answer.getLikes().stream()
                            .anyMatch(like -> like.getUserId().getUserId().equals(userId));

                    boolean canDeleteAnswer = userId != null && answer.getAnswerer().getUserId().equals(userId);
                    boolean canDeleteComment = userId != null && answer.getAnswerer().getUserId().equals(userId);

                    List<ReadAnswerCommentResDto> answerComments = answer.getComments().stream()
                            .map(comment -> ReadAnswerCommentResDto.builder()
                                    .commentId(comment.getId())
                                    .content(comment.getContent())
                                    .commenterId(comment.getCommenter().getUserId())
                                    .commenter(comment.getCommenter().getUsername())
                                    .profileImage(comment.getCommenter().getImageUrl())
                                    .createdAt(comment.getCreatedAt())
                                    .canDelete(canDeleteComment)
                                    .build())
                            .collect(Collectors.toList());

                    return ReadAnswerDto.builder()
                            .answerId(answer.getId())
                            .content(answer.getContent())
                            .answerId(answer.getId())
                            .questionId(questionBoard.getId())
                            .answererId(answer.getAnswerer().getUserId())
                            .answerer(answer.getAnswerer().getUsername())
                            .profileImg(answer.getAnswerer().getImageUrl())
                            .createdAt(answer.getCreatedAt())
                            .updatedAt(answer.getUpdatedAt())
                            .likeCount(answer.getLikes().size())
                            .isLiked(answerIsLiked)
                            .canDelete(canDeleteAnswer)
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
                .profileImg(questionBoard.getQuestioner().getImageUrl())
                .createdAt(questionBoard.getCreatedAt())
                .updatedAt(questionBoard.getUpdatedAt())
                .isLiked(isLiked)
                .isDisliked(isDisliked)
                .likeCount(questionLikeCount)
                .dislikeCount(questionDislikeCount)
                .canDelete(canDeleteQuestion)
                .answers(answers)
                .build();
    }

    // 삭제
    @Transactional
    public void deleteQuestionBoard(Long questionId) {
        QuestionBoardEntity questionBoard = questionBoardRepository.findById(questionId)
                .orElseThrow(
                        () -> new AppException(ErrorCode.QUES_BOARD_NOT_FOUND.getMessage(), ErrorCode.QUES_BOARD_NOT_FOUND));

        questionBoardRepository.delete(questionBoard);
    }


    // 전체 질문 게시판
    @Transactional
    public MainAllData findAllQuestionBoard(int page) {
        int pageSize = 5; // 페이지 크기

        // 전체 게시물 수 가져오기
        long totalItems = questionBoardRepository.count();

        // 전체 페이지 수 계산
        int totalPages = (int) Math.ceil((double) totalItems / pageSize);

        // 페이지 번호에 해당하는 게시물 가져오기
        Pageable pageable = PageRequest.of(page - 1, pageSize, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<QuestionBoardEntity> pageResult = questionBoardRepository.findAll(pageable);

        // 게시물 목록을 SearchQuestionDto로 변환
        List<SearchQuestionDto> questionBoards = pageResult.getContent().stream()
                .map(SearchQuestionDto::new)
                .collect(Collectors.toList());

        // PostData 객체에 데이터 저장하여 반환
        MainAllData mainAllData = new MainAllData();
        mainAllData.setCurrentPage(page);
        mainAllData.setTotalPages(totalPages);
        mainAllData.setPageSize(pageSize);
        mainAllData.setTotalItems(totalItems);
        mainAllData.setSearchQuestionDto(questionBoards);

        return mainAllData;
    }


    // 특정 id 질문 게시판
    @Transactional
    public Map<Integer, List<QuestionBoardResDto>> findQuestionBoardsByUserId(Long userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(
                        () -> new AppException(ErrorCode.USER_NOT_FOUND.getMessage(), ErrorCode.USER_NOT_FOUND));
        List<QuestionBoardEntity> questionBoardList = questionBoardRepository.findByQuestioner(user);

        Map<Integer, List<QuestionBoardResDto>> result = new HashMap<>();
        result.put(1, questionBoardList.stream()
                .map(QuestionBoardResDto::new)
                .collect(Collectors.toList()));

        return result;
    }


    @Transactional
    public SearchData findAllByKeywordContaining(String keyword, Pageable pageable) {
        // 게시물 검색 결과 가져오기
        Page<QuestionBoardEntity> matchedQuestionBoards = questionBoardRepository.findByTitleContainingOrContentContaining(keyword, keyword, pageable);

        // 유저 검색 결과 가져오기
        Page<UserEntity> matchedUsers = userRepository.findByUsernameContaining(keyword, pageable);

        // 게시물 검색 결과를 DTO로 변환
        List<SearchQuestionDto> searchQuestionDtos = matchedQuestionBoards.map(SearchQuestionDto::new).getContent();

        // 유저 검색 결과를 DTO로 변환
        List<SearchUserDto> searchUserDtos = matchedUsers.map(SearchUserDto::new).getContent();

        // SearchData 객체에 데이터 저장하여 반환
        SearchData searchData = new SearchData();
        searchData.setCurrentPage(pageable.getPageNumber() + 1);
        searchData.setTotalPages(matchedQuestionBoards.getTotalPages());
        searchData.setPageSize(pageable.getPageSize());
        searchData.setTotalItems((int) (matchedQuestionBoards.getTotalElements() + matchedUsers.getTotalElements()));
        searchData.setSearchQuestionDto(searchQuestionDtos);
        searchData.setSearchUserDto(searchUserDtos);

        return searchData;
    }

    @Transactional
    public SearchData findQuesBoardByTitleKeyword(String keyword, Pageable pageable) {
        // 질문 게시물 검색 결과 가져오기
        Page<QuestionBoardEntity> matchedQuestionBoards = questionBoardRepository.findByTitleContainingOrContentContaining(keyword, keyword, pageable);

        // 질문 게시물 검색 결과를 DTO로 변환
        List<SearchQuestionDto> searchQuestionDtos = matchedQuestionBoards.map(SearchQuestionDto::new).getContent();

        // SearchData 객체에 데이터 저장하여 반환
        SearchData searchData = new SearchData();
        searchData.setCurrentPage(pageable.getPageNumber() + 1);
        searchData.setTotalPages(matchedQuestionBoards.getTotalPages());
        searchData.setPageSize(pageable.getPageSize());
        searchData.setTotalItems((int) matchedQuestionBoards.getTotalElements());
        searchData.setSearchQuestionDto(searchQuestionDtos);

        return searchData;
    }

    @Transactional
    public SearchData findUserByUsernameKeyword(String keyword, Pageable pageable) {
        // 사용자 검색 결과 가져오기
        Page<UserEntity> matchedUsers = userRepository.findByUsernameContaining(keyword, pageable);

        // 사용자 검색 결과를 DTO로 변환
        List<SearchUserDto> searchUserDtos = matchedUsers.map(SearchUserDto::new).getContent();

        // SearchData 객체에 데이터 저장하여 반환
        SearchData searchData = new SearchData();
        searchData.setCurrentPage(pageable.getPageNumber() + 1);
        searchData.setTotalPages(matchedUsers.getTotalPages());
        searchData.setPageSize(pageable.getPageSize());
        searchData.setTotalItems((int) matchedUsers.getTotalElements());
        searchData.setSearchUserDto(searchUserDtos);

        return searchData;
    }

}
