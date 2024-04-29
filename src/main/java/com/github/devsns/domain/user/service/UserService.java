package com.github.devsns.domain.user.service;

import com.github.devsns.domain.answers.dto.AnswerResDto;
import com.github.devsns.domain.answers.entity.AnswerEntity;
import com.github.devsns.domain.answers.repository.AnswerRepository;
import com.github.devsns.domain.follow.dto.FollowResponseDto;
import com.github.devsns.domain.follow.entity.FollowEntity;
import com.github.devsns.domain.follow.repository.FollowRepository;
import com.github.devsns.domain.question.dto.QuestionBoardResDto;
import com.github.devsns.domain.question.entity.QuestionBoardEntity;
import com.github.devsns.domain.question.repository.QuestionBoardRepository;
import com.github.devsns.domain.user.dto.GetUserDto;
import com.github.devsns.domain.user.dto.SignupDto;
import com.github.devsns.domain.user.dto.UpdateUser;
import com.github.devsns.domain.user.dto.UserResponseDto;
import com.github.devsns.domain.user.entitiy.Role;
import com.github.devsns.domain.user.entitiy.UserEntity;
import com.github.devsns.domain.user.repository.UserRepository;
import com.github.devsns.exception.AppException;
import com.github.devsns.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final FollowRepository followRepository;
    private final PasswordEncoder passwordEncoder;
    private final QuestionBoardRepository questionBoardRepository;
    private final AnswerRepository answerRepository;

    // 회원가입
    @Transactional
    public void signup(SignupDto signupDto) throws Exception {
        // 이메일 중복 확인
        if (userRepository.findByEmail(signupDto.getEmail()).isPresent()) {
            throw new AppException(ErrorCode.USER_EMAIL_DUPLICATED.getMessage(), ErrorCode.USER_EMAIL_DUPLICATED);
        }

        // 유저네임 중복 확인
        if (userRepository.findByUsername(signupDto.getUsername()).isPresent()) {
            throw new AppException(ErrorCode.USERNAME_DUPLICATED.getMessage(), ErrorCode.USERNAME_DUPLICATED);
        }

        UserEntity userEntity = UserEntity.builder()
                .email(signupDto.getEmail())
                .password(signupDto.getPassword())
                .username(signupDto.getUsername())
                .imageUrl("anonymous.png")
                .description(" ")
                .role(Role.USER)
                .createdAt(LocalDateTime.now())
                .build();

        userEntity.passwordEncode(passwordEncoder);

        userRepository.save(userEntity);
    }

    // 마이페이지에 유저 가져오기
    @Transactional(readOnly = true)
    public UserResponseDto getMyPage(String email) {
        UserEntity user = userRepository.findByEmail(email).orElseThrow(
                () -> new AppException(ErrorCode.USER_EMAIL_NOT_FOUND.getMessage(), ErrorCode.USER_EMAIL_NOT_FOUND)
        );

        // 팔로우 관련 출력
        UserResponseDto userResponseDto = new UserResponseDto(user);

        Long followingCount = followRepository.countByFromUser(user);
        Long followerCount = followRepository.countByToUser(user);
        List<QuestionBoardEntity> questionBoardEntities = questionBoardRepository.findAllByQuestionerOrderByCreatedAtDesc(user);
        List<AnswerEntity> answerEntities = answerRepository.findAllByAnswererOrderByCreatedAtDesc(user);

        // 질문글 관련 출력
        List<QuestionBoardResDto> questionBoardResDtoList = new ArrayList<>();

        for (QuestionBoardEntity questionBoard : questionBoardEntities) {
            UserEntity userEntity = questionBoard.getQuestioner();
            QuestionBoardResDto questionBoardResDto = new QuestionBoardResDto(questionBoard);
            questionBoardResDtoList.add(questionBoardResDto);
        }

        // 답변글 관련 출력
        List<AnswerResDto> answerResDtoList = new ArrayList<>();

        for (AnswerEntity answer : answerEntities) {
            UserEntity userEntity = answer.getAnswerer();
            AnswerResDto answerResDto = new AnswerResDto(answer);
            answerResDtoList.add(answerResDto);
        }

        userResponseDto.setFollowingCount(followingCount);
        userResponseDto.setFollowerCount(followerCount);
        userResponseDto.setQuestionBoardResDtoList(questionBoardResDtoList);
        userResponseDto.setAnswerResDtoList(answerResDtoList);

        return userResponseDto;
    }

    // 유저정보 가져오기
    public GetUserDto getUser(String username) {
        UserEntity user = userRepository.findByUsername(username).orElseThrow(
                () -> new AppException(ErrorCode.USERNAME_NOT_FOUND.getMessage(), ErrorCode.USERNAME_NOT_FOUND)
        );

        GetUserDto getUserDto = new GetUserDto(user);

        Long followingCount = followRepository.countByFromUser(user);
        Long followerCount = followRepository.countByToUser(user);

        getUserDto.setFollowingCount(followingCount);
        getUserDto.setFollowerCount(followerCount);

        return getUserDto;
    }

    // 마이페이지에서 유저정보 업데이트
    @Transactional
    public UserResponseDto updateUser(String email, UpdateUser updateUser) {

        UserEntity user = userRepository.findByEmail(email).orElseThrow(
                () -> new AppException(ErrorCode.USER_EMAIL_NOT_FOUND.getMessage(), ErrorCode.USER_EMAIL_NOT_FOUND)
        );

        String encodedPassword = passwordEncoder.encode(updateUser.getPassword());

        user.setImageUrl(updateUser.getImageUrl());
        user.setPassword(encodedPassword);
        user.setUsername(updateUser.getUsername());
        user.setDescription(updateUser.getDescription());

        return new UserResponseDto(user);
    }

    // 회원 탈퇴
    public void deleteUser(String email) {
        UserEntity user = userRepository.findByEmail(email).orElseThrow(
                () -> new AppException(ErrorCode.USER_EMAIL_NOT_FOUND.getMessage(), ErrorCode.USER_EMAIL_NOT_FOUND)
        );

        if (userRepository.findByEmail(email).isPresent()) {
            userRepository.delete(user);
        }
    }

    // 유저네임으로 유저 찾기
    @Transactional(readOnly = true)
    public UserEntity findUser(String username) {

        return userRepository.findByUsername(username).orElseThrow(
                () -> new AppException(ErrorCode.USERNAME_NOT_FOUND.getMessage(), ErrorCode.USERNAME_NOT_FOUND)
        );
    }

}
