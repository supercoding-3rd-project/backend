package com.github.devsns.domain.user.controller;

import com.github.devsns.domain.auth.entity.CustomUserDetails;
import com.github.devsns.domain.user.dto.SignupDto;
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
    private final UserRepository userRepository;

    @PostMapping("/signup")
    public String signUp(@RequestBody SignupDto signupDto) throws Exception {
        userService.signup(signupDto);
        return "회원가입 성공";
    }

    @GetMapping("/jwt-test")
    public ResponseEntity<?> jwtTest(Authentication authentication) {
        String email =  authentication.getName();

        UserEntity user = userRepository.findByEmail(email).orElseThrow(
                () -> new AppException(ErrorCode.USER_EMAIL_NOT_FOUND.getMessage(), ErrorCode.USER_EMAIL_NOT_FOUND)
        );
        return ResponseEntity.ok(user);
    }

    @GetMapping("/jwt-test2")
    public ResponseEntity<?> jwtTest2(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        String email = customUserDetails.getUsername();

        UserEntity user = userRepository.findByEmail(email).orElseThrow(
                () -> new AppException(ErrorCode.USER_EMAIL_NOT_FOUND.getMessage(), ErrorCode.USER_EMAIL_NOT_FOUND)
        );
        return ResponseEntity.ok(user);
    }
}
