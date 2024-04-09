package com.github.devsns.domain.follow.dto;

import com.github.devsns.domain.user.entitiy.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FollowResponseDto {

    private String imageUrl;
    private String email;
    private String username;

    public FollowResponseDto(UserEntity userEntity) {
        this.imageUrl = userEntity.getImageUrl();
        this.email = userEntity.getEmail();
        this.username = userEntity.getUsername();
    }
}
