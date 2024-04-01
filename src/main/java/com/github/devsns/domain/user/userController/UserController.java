package com.github.devsns.domain.user.userController;

import com.github.devsns.domain.user.userDto.SignupDto;
import com.github.devsns.domain.user.userService.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    // 회원가입
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody SignupDto signupDto, BindingResult bindingResult){
        if(bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
        }
        userService.signUp(signupDto);

        return new ResponseEntity<String>("회원가입이 완료되었습니다.", HttpStatus.OK);
    }


}
