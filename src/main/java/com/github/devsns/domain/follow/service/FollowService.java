package com.github.devsns.domain.follow.service;

import com.github.devsns.domain.follow.entity.FollowEntity;
import com.github.devsns.domain.follow.repository.FollowRepository;
import com.github.devsns.domain.user.entitiy.UserEntity;
import com.github.devsns.domain.user.repository.UserRepository;
import com.github.devsns.exception.AppException;
import com.github.devsns.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class FollowService {

    private final FollowRepository followRepository;
    private final UserRepository userRepository;

    // 팔로잉
    @Transactional
    public void following(UserEntity fromUser, UserEntity toUser) {
        // 본인은 팔로잉 X
        if (fromUser == toUser) {
            throw new AppException(ErrorCode.FOLLOW_INVALID_REQUEST.getMessage(), ErrorCode.FOLLOW_INVALID_REQUEST);
        }

        // 중복 팔로우 X
        if (followRepository.findFollow(fromUser, toUser).isPresent()) {
            throw new AppException(ErrorCode.FOLLOW_DUPLICATED.getMessage(), ErrorCode.FOLLOW_DUPLICATED);
        }

        FollowEntity followEntity = FollowEntity.builder()
                .fromUser(fromUser)
                .toUser(toUser)
                .createdAt(LocalDateTime.now())
                .build();

        followRepository.save(followEntity);
    }

    @Transactional
    public void unfollow(String toUsername, String fromUsername) {
        UserEntity toUser = userRepository.findByUsername(toUsername).orElseThrow(
                () -> new AppException(ErrorCode.USERNAME_NOT_FOUND.getMessage(), ErrorCode.USERNAME_NOT_FOUND)
        );

        UserEntity fromUser = userRepository.findByUsername(fromUsername).orElseThrow(
                () -> new AppException(ErrorCode.USERNAME_NOT_FOUND.getMessage(), ErrorCode.USERNAME_NOT_FOUND)
        );

        followRepository.deleteByToUserAndFromUser(toUser, fromUser);
    }
}
