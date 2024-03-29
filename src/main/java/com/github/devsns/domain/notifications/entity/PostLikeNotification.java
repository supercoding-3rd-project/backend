package com.github.devsns.domain.notifications.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class PostLikeNotification  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User recipient; // 알림을 받는 사용자

    @ManyToOne
    private User liker; // 좋아요를 누른 사용자

    @ManyToOne
    private Post post; // 좋아요를 누른 글
}
