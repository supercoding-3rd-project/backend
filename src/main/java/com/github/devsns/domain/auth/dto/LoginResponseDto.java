package com.github.devsns.domain.auth.dto;

import com.github.devsns.domain.user.entitiy.UserEntity;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class LoginResponseDto {

    private Long userId;
    private String email;
    private String password;
    private String username;
    private String accessToken;
    private String refreshToken;
    private LocalDateTime createdAt;
    private LocalDateTime deletedAt;

    public LoginResponseDto(UserEntity userEntity) {
        this.userId = userEntity.getUserId();
        this.email = userEntity.getEmail();
        this.password = userEntity.getPassword();
        this.username = userEntity.getUsername();
        this.createdAt = userEntity.getCreatedAt();
        this.deletedAt = userEntity.getDeletedAt();
    }
}
