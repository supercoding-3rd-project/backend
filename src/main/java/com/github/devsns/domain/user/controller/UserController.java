package com.github.devsns.domain.user.controller;

import com.github.devsns.domain.auth.entity.CustomUserDetails;
import com.github.devsns.domain.user.dto.GetUserDto;
import com.github.devsns.domain.user.dto.SignupDto;
import com.github.devsns.domain.user.dto.UpdateUser;
import com.github.devsns.domain.user.dto.UserResponseDto;
import com.github.devsns.domain.user.service.UserService;
import com.github.devsns.exception.AppException;
import com.github.devsns.exception.ErrorCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import java.util.Objects;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "유저 관련 API", description = "유저 서비스 관련 api 컨트롤러")
public class UserController {

    private final UserService userService;

    // 회원가입
    @Operation(summary = "이메일, 비밀번호, 유저네임으로 회원가입을 처리하는 API 엔드포인트")
    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@Valid @RequestBody SignupDto signupDto, BindingResult bindingResult) throws Exception {
        if(bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
        }

        userService.signup(signupDto);
        return ResponseEntity.ok("회원가입 성공");
    }

    // 마이페이지
    @Operation(summary = "로그인한 유저의 정보를 가져와 마이페이지를 보여주는 API 컨트롤러")
    @GetMapping("/v1/user/myPage")
    public ResponseEntity<?> getMyPage(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        String email = customUserDetails.getUsername();
        UserResponseDto userResponseDto = userService.getMyPage(email);

        return ResponseEntity.ok(userResponseDto);
    }

    // 유저정보 가져오기
    @Operation(summary = "로그인 할 필요없이 유저네임을 이용해서 해당하는 유저의 프로필 이미지, 이메일, 유저네임, 팔로잉 수, 팔로워 수를 보여주는 API 컨트롤러")
    @GetMapping("/user/{username}")
    public ResponseEntity<?> getUser(@PathVariable("username") String username) {
        userService.getUser(username);
        GetUserDto getUserDto = userService.getUser(username);

        return ResponseEntity.ok(getUserDto);
    }

    // 유저정보 추가 입력
    @Operation(summary = "회원가입 시 부족했던 유저 정보를 추가하거나, 이미 저장된 유저 정보를 수정하는 API 컨트롤러(유저네임, 비밀번호, 프로필 이미지만 변경 가능")
    @PutMapping("/v1/user/update/{userId}")
    public ResponseEntity<?> updateUser(
            @PathVariable Long userId,
            @RequestBody @Valid UpdateUser updateUser,
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            BindingResult bindingResult) {

        // 유저아이디가 현재 로그인한 유저의 아이디와 일치하는지 확인
        if (!Objects.equals(userId, customUserDetails.getUserEntity().getUserId())) {
            throw new AppException(ErrorCode.USER_ID_UNMATCHED.getMessage(), ErrorCode.USER_ID_UNMATCHED);
        }

        if(bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
        }

        String email = customUserDetails.getUsername();
        UserResponseDto userResponseDto = userService.updateUser(email, updateUser);

        return ResponseEntity.ok(userResponseDto);
    }

    // 유저 삭제
    // TODO: 유저 탈퇴 시 cascade 적용해서 전부 삭제 구현해야함
    @Operation(summary = "로그인한 유저를 DB에서 삭제하는 API 컨트롤러")
    @DeleteMapping("/v1/user/delete/{userId}")
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
