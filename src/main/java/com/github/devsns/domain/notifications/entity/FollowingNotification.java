package com.github.devsns.domain.notifications.entity;

import com.github.devsns.domain.user.userEntity.UserEntity;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class FollowingNotification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private UserEntity recipient;
    @ManyToOne
    private UserEntity follower;

    @Column(nullable = false)
    private LocalDateTime createdAt;
}
