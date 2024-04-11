package com.github.devsns.domain.notifications.repository;

import com.github.devsns.domain.notifications.entity.AnswerNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnswerNotificationRepository extends JpaRepository<AnswerNotification, Long> {
    void deleteByAnswererIdAndQuestionId(Long answererId, Long questionId);
}