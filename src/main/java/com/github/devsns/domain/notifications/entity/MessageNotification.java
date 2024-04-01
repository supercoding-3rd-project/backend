package com.github.devsns.domain.notifications.entity;

import com.github.devsns.domain.user.userEntity.UserEntity;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class MessageNotification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private UserEntity recipient; // 알림을 받는 사용자

    @ManyToOne
    private UserEntity sender; // 메시지를 보낸 사용자
}
