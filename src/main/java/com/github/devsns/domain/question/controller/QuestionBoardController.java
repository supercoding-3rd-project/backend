package com.github.devsns.domain.question.controller;

import com.github.devsns.domain.auth.entity.CustomUserDetails;
import com.github.devsns.domain.question.dto.*;
import com.github.devsns.domain.question.dto.like.LikeQuestionDto;
import com.github.devsns.domain.question.dto.read.ReadQuestionDto;
import com.github.devsns.domain.question.dto.search.MainAllData;
import com.github.devsns.domain.question.dto.search.SearchData;
import com.github.devsns.domain.question.repository.QuestionBoardRepository;
import com.github.devsns.domain.question.service.QuestionBoardService;
import com.github.devsns.domain.user.entitiy.UserEntity;
import com.github.devsns.domain.user.repository.UserRepository;
import com.github.devsns.global.constant.LikeType;
import com.github.devsns.global.jwt.service.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Slf4j
@Tag(name = "질문 및 검색 API", description = "질문 C, R, D 및 좋아요 C,D, 유저 ID / 키워드 검색")
public class QuestionBoardController {
    private final UserRepository userRepository;
    private final QuestionBoardService questionBoardService;
    private final JwtService jwtService;
    private final QuestionBoardRepository questionBoardRepository;

    @GetMapping("/search")
    @Operation(summary = "메인 화면 전체 게시물 출력")
    public MainAllData findAllQuestionBoard(@RequestParam(defaultValue = "1") int page) {
        MainAllData mainAllData = questionBoardService.findAllQuestionBoard(page);
        return mainAllData;
    }

    @GetMapping("/search/user/{userId}")
    @Operation(summary = "USER ID 통해서 작성한 게시물 출력")
    public Map<Integer, List<QuestionBoardResDto>> findByQuestionBoardId(@PathVariable Long userId) {
        return questionBoardService.findQuestionBoardsByUserId(userId);
    }

    @GetMapping("/search/keyword/all")
    @Operation(summary = "KEYWORD 통해서 질문 게시물(제목, 내용) / 유저 이름 검색")
    public SearchData findAllByKeywordContaining(@RequestParam("keyword") String keyword,
                                                 @RequestParam(value = "page", defaultValue = "1") int page,
                                                 @RequestParam(value = "size", defaultValue = "5") int size) {
        return questionBoardService.findAllByKeywordContaining(keyword, PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createdAt")));
    }

    @GetMapping("/search/keyword/question")
    @Operation(summary = "KEYWORD 통해서 질문 게시물(제목, 내용) 검색")
    public SearchData findQuesBoardByTitleKeyword(@RequestParam("keyword") String keyword,
                                                  @RequestParam(value = "page", defaultValue = "1") int page,
                                                  @RequestParam(value = "size", defaultValue = "5") int size) {
        return questionBoardService.findQuesBoardByTitleKeyword(keyword, PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createdAt")));
    }

    @GetMapping("/search/keyword/user")
    @Operation(summary = "KEYWORD 통해서 유저 이름 검색")
    public SearchData findUserByUsernameKeyword(@RequestParam("keyword") String keyword,
                                                @RequestParam(value = "page", defaultValue = "1") int page,
                                                @RequestParam(value = "size", defaultValue = "5") int size) {
        return questionBoardService.findUserByUsernameKeyword(keyword, PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createdAt")));
    }





    @PostMapping("/v1/question/create")
    @Operation(summary = "질문 작성 페이지에서 임시 재생 목록")
    public List<TempQuestionResDto> getTemporarySavedQuestions(@AuthenticationPrincipal CustomUserDetails user) {
        List<TempQuestionResDto> result = questionBoardService.getTemporarySavedQuestions(user.getUsername());
        return result;
    }

    @PostMapping("/v1/question/create/submit")
    @Operation(summary = "질문 저장")
    public ResponseEntity<?> submitQuestion(@RequestBody QuestionBoardReqDto questionBoardReqDto,@AuthenticationPrincipal CustomUserDetails user) {
        QuestionCreateDto result = questionBoardService.submitQuestion(questionBoardReqDto, user.getUsername());
        return ResponseEntity.ok(result);
    }

    @PatchMapping("/v1/question/create/temp")
    @Operation(summary = "임시 저장")
    public ResponseEntity<List<TempQuestionResDto>> saveTemporaryQuestion(@RequestBody TempQuestionReqDto tempQuestionReqDto, @AuthenticationPrincipal CustomUserDetails user) {
        List<TempQuestionResDto> result = questionBoardService.saveTemporaryQuestion(tempQuestionReqDto, user.getUsername());
        return ResponseEntity.ok(result);
    }


    @PatchMapping("/v1/question/temp/{tempId}/delete")
    @Operation(summary = "임시 저장 글 지우기")
    public ResponseEntity<List<TempQuestionResDto>> deleteTemporaryQuestion(@PathVariable Long tempId, @AuthenticationPrincipal CustomUserDetails user) {
        List<TempQuestionResDto> result = questionBoardService.deleteTemporaryQuestion(tempId, user.getUsername());
        return ResponseEntity.ok(result);
    }


    @DeleteMapping("/v1/question/{quesId}/delete")
    @Operation(summary = "질문 삭제 => 질문 삭제 시 모든 연관 요소 삭제")
    public ResponseEntity<?> deleteQuestion(@PathVariable Long quesId) {
        questionBoardService.deleteQuestionBoard(quesId);
        return ResponseEntity.ok("질문이 삭제되었습니다.");
    }

    @GetMapping("/question/{quesId}")
    @Operation(summary = "하나의 질문 조회")
    public ResponseEntity<?> readQuestionBoard(@PathVariable Long quesId, HttpServletRequest request) {

        String token = request.getHeader("Authorization");
        Long userId = null;

        if (token != null) {
            token = token.replace("Bearer ", "");
            Optional<String> email = jwtService.extractEmail(token);
//            userId = email.map(e -> userRepository.findByEmail(e).map(UserEntity::getUserId).orElse(null)).orElse(null);
            userId = email.flatMap(e -> userRepository.findByEmail(e))
                    .map(UserEntity::getUserId)
                    .orElse(null);
        }

        ReadQuestionDto readQuestionDto = questionBoardService.getQuestionBoardDetails(quesId, userId);
        return ResponseEntity.ok(readQuestionDto);
    }


    @PatchMapping("/v1/question/{quesId}/like")
    @Operation(summary = "질문에 좋아요 및 좋아요 취소 / 최초 좋아요면 저장, 이미 좋아요 내역 있으면 취소, 싫어요 있으면 수행 X")
    public ResponseEntity<?> toggleLikeQuestionBoard(@PathVariable Long quesId, @AuthenticationPrincipal CustomUserDetails user) {
        try {
        // Authentication 객체에서 사용자 ID 추출
        LikeQuestionDto result = questionBoardService.updateQuestionReaction(quesId, user.getUserEntity().getUserId(), LikeType.LIKE);
        return ResponseEntity.ok(result);
        } catch (NoSuchElementException e) {
            return ResponseEntity.badRequest().body("답변이 존재하지 않습니다");
        }

    }

    @PatchMapping("/v1/question/{quesId}/dislike")
    @Operation(summary = "질문에 좋아요 및 좋아요 취소 / 최초 좋아요면 저장, 이미 좋아요 내역 있으면 취소, 싫어요 있으면 수행 X")
    public ResponseEntity<?> toggleDislikeQuestionBoard(@PathVariable Long quesId, @AuthenticationPrincipal CustomUserDetails user) {
        try {
            // Authentication 객체에서 사용자 ID 추출
            LikeQuestionDto result = questionBoardService.updateQuestionReaction(quesId, user.getUserEntity().getUserId(), LikeType.DISLIKE);
            return ResponseEntity.ok(result);
        } catch (NoSuchElementException e) {
            return ResponseEntity.badRequest().body("답변이 존재하지 않습니다");
        }


    }





}
