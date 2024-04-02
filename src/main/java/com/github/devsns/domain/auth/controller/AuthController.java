package com.github.devsns.domain.auth.controller;

import com.github.devsns.domain.auth.dto.LoginRequestDto;
import com.github.devsns.domain.auth.dto.LoginResponseDto;
import com.github.devsns.domain.auth.dto.RefreshTokenDto;
import com.github.devsns.domain.auth.service.AuthService;
import com.github.devsns.domain.user.dto.SignupDto;
import com.github.devsns.domain.userDetails.entity.CustomUserDetails;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AuthController {

    private final AuthService authService;

    // 회원가입
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody SignupDto signupDto, BindingResult bindingResult){
        if(bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
        }
        authService.signUp(signupDto);

        return new ResponseEntity<String>("회원가입이 완료되었습니다.", HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequestDto loginRequestDto,
                                   HttpServletResponse response,
                                   BindingResult bindingResult){

        if(bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
        }

        LoginResponseDto loginResponseDto = authService.login(loginRequestDto, response);

        return ResponseEntity.ok(loginResponseDto);
    }

//    @GetMapping("/admin")
//    public String admin(){
//        return "관리자입니다.";
//    }
//
    @GetMapping("/test")
    public String test(@AuthenticationPrincipal CustomUserDetails customUserDetails){

        return customUserDetails.getEmail();
    }

    @PostMapping("/refreshToken")
    public ResponseEntity<String> refresh (@RequestBody RefreshTokenDto refreshTokenDto, HttpServletResponse response) {
        String message = authService.refresh(refreshTokenDto, response);
        return ResponseEntity.ok(message);
    }
}
