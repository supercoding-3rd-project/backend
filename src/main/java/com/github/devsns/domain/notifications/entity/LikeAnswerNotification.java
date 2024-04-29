package com.github.devsns.domain.notifications.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "like_answer_notification")
public class LikeAnswerNotification extends Notification {

    @Column(name = "liker_id")
    private Long likerId; // 좋아요를 누른 사용자

    @Column(name = "liker")
    private String liker;

    @Column(name = "profile_image")
    private String profileImage;

    @Column(name = "answer_id")
    private Long answerId; // 좋아요를 누른 글

}
