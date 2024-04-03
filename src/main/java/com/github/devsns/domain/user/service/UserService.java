package com.github.devsns.domain.user.service;

import com.github.devsns.domain.user.dto.SignupDto;
import com.github.devsns.domain.user.entitiy.Role;
import com.github.devsns.domain.user.entitiy.UserEntity;
import com.github.devsns.domain.user.repository.UserRepository;
import com.github.devsns.exception.AppException;
import com.github.devsns.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void signup(SignupDto signupDto) throws Exception {
        // 이메일 중복 확인
        if (userRepository.findByEmail(signupDto.getEmail()).isPresent()) {
            throw new AppException(ErrorCode.USER_EMAIL_NOT_FOUND.getMessage(), ErrorCode.USER_EMAIL_NOT_FOUND);
        }

        // 유저네임 중복 확인
        if (userRepository.findByUsername(signupDto.getUsername()).isPresent()) {
            throw new AppException(ErrorCode.USERNAME_DUPLICATED.getMessage(), ErrorCode.USERNAME_DUPLICATED);
        }

        UserEntity userEntity = UserEntity.builder()
                .email(signupDto.getEmail())
                .password(signupDto.getPassword())
                .username(signupDto.getPassword())
                .role(Role.USER)
                .build();

        userEntity.passwordEncode(passwordEncoder);

        userRepository.save(userEntity);
    }

}
