package com.github.devsns.domain.notifications.entity;

import com.github.devsns.domain.user.entitiy.UserEntity;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Data
@Table(name = "follow_notification")
public class FollowNotification extends Notification {

    @Column(name = "follower_id")
    private Long followerId; // 팔로우를 한 사용자

}