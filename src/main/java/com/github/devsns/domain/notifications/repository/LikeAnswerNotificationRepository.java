package com.github.devsns.domain.notifications.repository;

import com.github.devsns.domain.notifications.entity.LikeAnswerNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeAnswerNotificationRepository extends JpaRepository<LikeAnswerNotification, Long> {

}