package com.github.devsns.domain.notifications.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "like_question_notification")
public class LikeQuestionNotification extends Notification {

    @Column(name = "liker_id")
    private Long likerId; // 좋아요를 누른 사용자

    @Column(name = "liker")
    private String liker;

    @Column(name = "profile_image")
    private String profileImage;

    @Column(name = "question_id")
    private Long questionId; // 좋아요를 누른 글
}
