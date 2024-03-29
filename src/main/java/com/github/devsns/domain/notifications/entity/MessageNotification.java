package com.github.devsns.domain.notifications.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class MessageNotification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User recipient; // 알림을 받는 사용자

    @ManyToOne
    private User sender; // 메시지를 보낸 사용자
}
