package com.github.devsns.domain.auth.service;

import com.github.devsns.domain.auth.dto.LoginRequestDto;
import com.github.devsns.domain.auth.dto.LoginResponseDto;
import com.github.devsns.domain.auth.dto.RefreshTokenDto;
import com.github.devsns.domain.auth.entity.RefreshTokenEntity;
import com.github.devsns.domain.auth.repository.RefreshTokenRepository;
import com.github.devsns.domain.role.entity.RoleEntity;
import com.github.devsns.domain.role.repository.RoleRepository;
import com.github.devsns.domain.user.dto.SignupDto;
import com.github.devsns.domain.user.entitiy.UserEntity;
import com.github.devsns.domain.user.entitiy.UserRoleEntity;
import com.github.devsns.domain.user.repository.UserRepository;
import com.github.devsns.domain.user.repository.UserRoleRepository;
import com.github.devsns.exception.AppException;
import com.github.devsns.exception.ErrorCode;
import com.github.devsns.global.jwt.util.Constants;
import com.github.devsns.global.jwt.util.JwtTokenUtils;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtils jwtTokenUtils;
    private final PasswordEncoder passwordEncoder;

    // 회원가입
    @Transactional
    public void signUp(SignupDto signupDto){
        // 입력한 값들 받아오기
        UserEntity userEntity = UserEntity.builder()
                .email(signupDto.getEmail())
                .password(passwordEncoder.encode(signupDto.getPassword()))
                .username(signupDto.getUsername())
                .createdAt(LocalDateTime.now())
                .build();

        RoleEntity roleEntity = roleRepository.findByRoleName("ROLE_USER").orElseThrow(
                () -> new AppException(ErrorCode.ROLE_NOT_FOUND.getMessage(), ErrorCode.ROLE_NOT_FOUND)
        );

        // 이메일 중복 체크
        if (userRepository.findByEmail(signupDto.getEmail()).isPresent()){
            throw new AppException(ErrorCode.EMAIL_DUPLICATED.getMessage(), ErrorCode.EMAIL_DUPLICATED);
        }

        // 유저네임 중복체크
        if (userRepository.findByUsername(signupDto.getUsername()).isPresent()){
            throw new AppException(ErrorCode.USERNAME_DUPLICATED.getMessage(), ErrorCode.USERNAME_DUPLICATED);
        }

        // 회원정보 저장
        userRepository.save(userEntity);

        UserRoleEntity userRole = UserRoleEntity.builder()
                .user(userEntity)
                .role(roleEntity)
                .build();

        userRoleRepository.save(userRole);
    }

    // 로그인
    public LoginResponseDto login(LoginRequestDto loginRequestDto, HttpServletResponse response){

        Authentication auth = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequestDto.getEmail(), loginRequestDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(auth);

        UserEntity user = userRepository.findByEmail(loginRequestDto.getEmail()).orElseThrow(
                () -> new AppException(ErrorCode.USER_EMAIL_NOT_FOUND.getMessage(), ErrorCode.USER_EMAIL_NOT_FOUND)
        );

        if (user.getDeletedAt() != null){
            throw new AppException(ErrorCode.USER_EMAIL_NOT_FOUND.getMessage(), ErrorCode.USER_EMAIL_NOT_FOUND);
        }

        if (passwordEncoder.matches(user.getPassword(), loginRequestDto.getPassword())){
            throw new AppException(ErrorCode.INVALID_PASSWORD.getMessage(), ErrorCode.INVALID_PASSWORD);
        }

        LoginResponseDto loginResponseDto = new LoginResponseDto(user);

        String accessToken = jwtTokenUtils.createAccessToken(user.getEmail());
        response.addHeader(Constants.HEADER_ACCESSTOKEN_KEY, accessToken);
        Optional<RefreshTokenEntity> refreshToken = refreshTokenRepository.findByUserEntityUserId(user.getUserId());

        loginResponseDto.setAccessToken(accessToken);

        if(refreshToken.isEmpty()) {
            String newRefreshToken = jwtTokenUtils.createRefreshToken(user.getEmail());

            RefreshTokenEntity newRefreshTokenEntity = RefreshTokenEntity.builder()
                    .userEntity(user)
                    .refreshToken(newRefreshToken)
                    .build();

            refreshTokenRepository.save(newRefreshTokenEntity);
            response.addHeader(Constants.HEADER_REFRESHTOKEN_KEY, newRefreshToken);
            loginResponseDto.setRefreshToken(newRefreshToken);
        } else {
            response.addHeader(Constants.HEADER_REFRESHTOKEN_KEY, refreshToken.get().getRefreshToken());
            loginResponseDto.setRefreshToken(refreshToken.get().getRefreshToken());
        }

        return loginResponseDto;
    }

    // 리프레시 토큰 발급
    public String refresh(RefreshTokenDto refreshTokenDto, HttpServletResponse response) {
        if( refreshTokenDto.getRefreshToken() == null) {
            throw new AppException(ErrorCode.REFRESH_TOKEN_NOT_FOUND.getMessage(), ErrorCode.REFRESH_TOKEN_NOT_FOUND);
        }

        UserEntity user = userRepository.findByEmail(refreshTokenDto.getEmail()).orElseThrow(
                () -> new AppException(ErrorCode.USER_EMAIL_NOT_FOUND.getMessage(), ErrorCode.USER_EMAIL_NOT_FOUND)
        );

        RefreshTokenEntity refreshToken = refreshTokenRepository.findByUserEntityUserId(user.getUserId()).orElseThrow(
                () -> new AppException(ErrorCode.REFRESH_TOKEN_NOT_FOUND.getMessage(), ErrorCode.REFRESH_TOKEN_NOT_FOUND)
        );

        if (jwtTokenUtils.isExpired(refreshToken.getRefreshToken())) {
            refreshTokenRepository.delete(refreshToken);
            throw new AppException(ErrorCode.EXPIRED_TOKEN.getMessage(), ErrorCode.EXPIRED_TOKEN);
        }

        if (!refreshToken.getRefreshToken().equals(refreshTokenDto.getRefreshToken())) {
            throw new AppException(ErrorCode.RESOLVE_TOKEN_PROBLEM.getMessage(), ErrorCode.RESOLVE_TOKEN_PROBLEM);
        }

        response.addHeader(Constants.HEADER_ACCESSTOKEN_KEY, jwtTokenUtils.createAccessToken(refreshTokenDto.getEmail()));

        return "accessToken이 발급 되었습니다.";
    }

}
