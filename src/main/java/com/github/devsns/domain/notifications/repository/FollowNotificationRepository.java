package com.github.devsns.domain.notifications.repository;

import com.github.devsns.domain.notifications.entity.FollowNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FollowNotificationRepository extends JpaRepository<FollowNotification, Long> {
    void deleteByRecipientIdAndFollowerId(Long recipientId, Long followerId) ;
    // 필요한 메서드 추가
}