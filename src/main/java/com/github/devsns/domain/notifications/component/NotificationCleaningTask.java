package com.github.devsns.domain.notifications.component;

import com.github.devsns.domain.notifications.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class NotificationCleaningTask {
    private final NotificationRepository notificationRepository;


    @Scheduled(cron = "0 * * * * *") // 매 분 0초마다 실행
    public void cleanupNotifications() {
        // 현재 시간으로부터 24시간 이전의 시간 계산
        LocalDateTime dateTime = LocalDateTime.now().minusHours(24);
        // 해당 시간 이전의 알림 삭제
        notificationRepository.deleteByCreatedAtBefore(dateTime);
    }
}
