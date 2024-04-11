package com.github.devsns.domain.notifications.repository;

import com.github.devsns.domain.notifications.entity.Notification;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
@Primary
@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findAllByOrderByCreatedAtDesc();

    List<Notification>findAllByRecipientId(Long recipientId);

    void deleteByCreatedAtBefore(LocalDateTime dateTime);
}
