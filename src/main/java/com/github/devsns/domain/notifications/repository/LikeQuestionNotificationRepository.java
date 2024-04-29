package com.github.devsns.domain.notifications.repository;

import com.github.devsns.domain.notifications.entity.LikeQuestionNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeQuestionNotificationRepository extends JpaRepository<LikeQuestionNotification, Long> {
    void deleteByRecipientIdAndLikerId(Long recipientId, Long likerId);
}
