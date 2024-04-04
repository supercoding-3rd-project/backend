package com.github.devsns.domain.auth.dto;

import com.github.devsns.domain.user.entitiy.Role;
import com.github.devsns.domain.user.entitiy.UserEntity;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponseDto {
    private Long userId;
    private String email;
    private String password;
    private String username;
    private String imageUrl;
    private Role role;
    private String accessToken;
    private String refreshToken;

    public LoginResponseDto(UserEntity userEntity) {
        this.userId = userEntity.getUserId();
        this.email = userEntity.getEmail();
        this.password = userEntity.getPassword();
        this.username = userEntity.getUsername();
        this.imageUrl = userEntity.getImageUrl();
        this.role = userEntity.getRole();
    }

}
