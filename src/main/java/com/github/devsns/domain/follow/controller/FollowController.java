package com.github.devsns.domain.follow.controller;

import com.github.devsns.domain.auth.entity.CustomUserDetails;
import com.github.devsns.domain.follow.dto.FollowResponseDto;
import com.github.devsns.domain.follow.entity.FollowEntity;
import com.github.devsns.domain.follow.repository.FollowRepository;
import com.github.devsns.domain.follow.service.FollowService;
import com.github.devsns.domain.user.entitiy.UserEntity;
import com.github.devsns.domain.user.repository.UserRepository;
import com.github.devsns.domain.user.service.UserService;
import com.github.devsns.exception.AppException;
import com.github.devsns.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class FollowController {
    private final UserService userService;
    private final FollowService followService;
    private final UserRepository userRepository;
    private final FollowRepository followRepository;

    // 팔로잉
    @PostMapping("/v1/user/follow/{username}")
    public ResponseEntity<String> following(@AuthenticationPrincipal CustomUserDetails customUserDetails, @PathVariable("username") String username) {

        UserEntity fromUser = userService.findUser(customUserDetails.getUserEntity().getUsername());
        UserEntity toUser = userService.findUser(username);

        if (userRepository.findByUsername(username).isEmpty()) {
            throw new AppException(ErrorCode.USERNAME_NOT_FOUND.getMessage(), ErrorCode.USERNAME_NOT_FOUND);
        } else {
            followService.following(fromUser, toUser);
        }

        return ResponseEntity.ok(username + " 을(를) 팔로우 했습니다.");
    }

    // 팔로우 취소
    @DeleteMapping("/v1/user/follow/{username}")
    public ResponseEntity<?> unfollow(@AuthenticationPrincipal CustomUserDetails customUserDetails, @PathVariable("username") String username) {

        String loginUsername = customUserDetails.getUserEntity().getUsername();

        UserEntity fromUser = userRepository.findByUsername(loginUsername).orElseThrow(
                () -> new AppException(ErrorCode.USERNAME_NOT_FOUND.getMessage(), ErrorCode.USERNAME_NOT_FOUND)
        );

        UserEntity toUser = userRepository.findByUsername(username).orElseThrow(
                () -> new AppException(ErrorCode.USERNAME_NOT_FOUND.getMessage(), ErrorCode.USERNAME_NOT_FOUND)
        );

        if (followRepository.findByFromUser(fromUser).isEmpty() || followRepository.findByToUser(toUser).isEmpty()) {
            throw new AppException(ErrorCode.FOLLOW_NOT_FOUND.getMessage(), ErrorCode.FOLLOW_NOT_FOUND);
        }

        followService.unfollow(username, loginUsername);

        return ResponseEntity.ok("팔로우 취소");
    }

    // 해당 유저의 팔로잉 리스트 조회
    @GetMapping("/user/following/{username}")
    public ResponseEntity<List<FollowResponseDto>> getFollowing(@PathVariable("username") String username) {

        List<FollowResponseDto> followResponseDtoList;

        followResponseDtoList = followService.getFollowing(username);


        return ResponseEntity.ok(followResponseDtoList);
    }

    // 해당 유저의 팔로워 리스트 조회
    @GetMapping("/user/follower/{username}")
    public ResponseEntity<List<FollowResponseDto>> getFollower(@PathVariable("username") String username) {

        List<FollowResponseDto> followResponseDtoList;

        followResponseDtoList = followService.getFollower(username);


        return ResponseEntity.ok(followResponseDtoList);
    }

}
