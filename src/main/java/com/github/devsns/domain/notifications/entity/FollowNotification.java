package com.github.devsns.domain.notifications.entity;

import com.github.devsns.domain.user.entitiy.UserEntity;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class FollowNotification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private UserEntity recipient; // 알림을 받는 사용자

    @ManyToOne
    private UserEntity follower; // 팔로우를 한 사용자

}