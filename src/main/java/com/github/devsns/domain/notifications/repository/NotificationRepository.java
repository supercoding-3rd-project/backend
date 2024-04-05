package com.github.devsns.domain.notifications.repository;

import com.github.devsns.domain.notifications.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findFirst10ByRecipient_UserIdOrderByCreatedAtDesc(Long userId);
    List<Notification>findAllByRecipientUserIdOrderByCreatedAtDesc(Long userId);

   List<Notification> deleteByCreatedAtBefore(LocalDateTime dateTime);
}