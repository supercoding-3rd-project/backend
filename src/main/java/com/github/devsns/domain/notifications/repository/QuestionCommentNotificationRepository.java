package com.github.devsns.domain.notifications.repository;

import com.github.devsns.domain.notifications.entity.AnswerCommentNotification;
import com.github.devsns.domain.notifications.entity.QuestionCommentNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionCommentNotificationRepository extends JpaRepository<QuestionCommentNotification, Long> {
    void deleteByCommentId(Long commentId);
}
