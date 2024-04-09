package com.github.devsns.domain.user.controller;

import com.github.devsns.domain.auth.entity.CustomUserDetails;
import com.github.devsns.domain.user.dto.SignupDto;
import com.github.devsns.domain.user.dto.UpdateUser;
import com.github.devsns.domain.user.dto.UserResponseDto;
import com.github.devsns.domain.user.entitiy.UserEntity;
import com.github.devsns.domain.user.repository.UserRepository;
import com.github.devsns.domain.user.service.UserService;
import com.github.devsns.exception.AppException;
import com.github.devsns.exception.ErrorCode;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    // 회원가입
    @PostMapping("/signup")
    public String signUp(@RequestBody SignupDto signupDto) throws Exception {
        userService.signup(signupDto);
        return "회원가입 성공";
    }

    // 마이페이지
    @GetMapping("/user/myPage")
    public ResponseEntity<?> getUser(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        String email = customUserDetails.getUsername();
        UserResponseDto userResponseDto = userService.getUser(email);

        return ResponseEntity.ok(userResponseDto);
    }

    // 유저정보 추가 입력
    // TODO: 이미지 업로드 구현해야함
    @PutMapping("/user/update/{userId}")
    public ResponseEntity<?> updateUser(
            @PathVariable Long userId,
            @RequestBody UpdateUser updateUser,
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        // 유저아이디가 현재 로그인한 유저의 아이디와 일치하는지 확인
        if (!Objects.equals(userId, customUserDetails.getUserEntity().getUserId())) {
            throw new AppException(ErrorCode.USER_ID_UNMATCHED.getMessage(), ErrorCode.USER_ID_UNMATCHED);
        }

        String email = customUserDetails.getUsername();
        UserResponseDto userResponseDto = userService.updateUser(email, updateUser);

        return ResponseEntity.ok(userResponseDto);
    }

    // 유저 삭제
    // TODO: 유저 탈퇴 시 cascade 적용해서 전부 삭제 구현해야함
    @DeleteMapping("/user/delete/{userId}")
    public ResponseEntity<?> deleteUser(
            @PathVariable Long userId,
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        // 유저아이디가 현재 로그인한 유저의 아이디와 일치하는지 확인
        if (!Objects.equals(userId, customUserDetails.getUserEntity().getUserId())) {
            throw new AppException(ErrorCode.USER_ID_UNMATCHED.getMessage(), ErrorCode.USER_ID_UNMATCHED);
        }

        String email = customUserDetails.getUsername();
        userService.deleteUser(email);

        return ResponseEntity.ok("회원탈퇴가 완료되었습니다.");
    }
}
