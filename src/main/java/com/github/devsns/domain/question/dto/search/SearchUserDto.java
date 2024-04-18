package com.github.devsns.domain.question.dto.search;

import com.github.devsns.domain.user.entitiy.UserEntity;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Builder
public class SearchUserDto {

    private Long userId;
    private String imageUrl;
    private String username;
    private int followingCount;
    private int followerCount;


    public SearchUserDto(UserEntity user) {
        this.userId = user.getUserId();
        this.imageUrl = user.getImageUrl();
        this.username = user.getUsername();
        this.followingCount = user.getFollowings().size();
        this.followerCount = user.getFollowers().size();
    }


}
