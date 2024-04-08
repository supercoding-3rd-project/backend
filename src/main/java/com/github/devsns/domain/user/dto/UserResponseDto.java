package com.github.devsns.domain.user.dto;

import com.github.devsns.domain.user.entitiy.SocialType;
import com.github.devsns.domain.user.entitiy.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponseDto {

    private Long userId;
    private String role;
    private String imageUrl;
    private String email;
    private String password;
    private String username;
    private String socialId;
    private SocialType socialType;
    private LocalDateTime createdAt;

    public UserResponseDto(UserEntity userEntity){
        this.userId = userEntity.getUserId();
        this.role = userEntity.getRole().toString();
        this.imageUrl = userEntity.getImageUrl();
        this.email = userEntity.getEmail();
        this.password = userEntity.getPassword();
        this.username = userEntity.getUsername();
        this.socialId = userEntity.getSocialId();
        this.socialType = userEntity.getSocialType();
        this.createdAt = userEntity.getCreatedAt();
    }
}
