package com.github.devsns.domain.user.dto;

import com.github.devsns.domain.user.entitiy.UserEntity;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetUserDto {

    private String imageUrl;
    private String email;
    private String username;
    private Long followingCount;
    private Long followerCount;

    public GetUserDto(UserEntity userEntity) {
        this.imageUrl = userEntity.getImageUrl();
        this.email = userEntity.getEmail();
        this.username = userEntity.getUsername();
    }

}
