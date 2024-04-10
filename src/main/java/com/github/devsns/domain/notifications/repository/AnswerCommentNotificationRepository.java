package com.github.devsns.domain.notifications.repository;

import com.github.devsns.domain.notifications.entity.AnswerCommentNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnswerCommentNotificationRepository extends JpaRepository<AnswerCommentNotification, Long> {
    void deleteByCommentId(Long commentId);
}