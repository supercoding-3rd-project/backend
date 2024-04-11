package com.github.devsns.domain.notifications.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "follow_notification")
public class FollowNotification extends Notification {

    @Column(name = "follower_id")
    private Long followerId; // 팔로우를 한 사용자

}