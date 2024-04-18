package com.github.devsns.domain.chat.repository;

import com.github.devsns.domain.chat.entity.ChatMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long>, QuerydslPredicateExecutor<ChatMessage> {
    // 페이징을 사용하여 특정 채팅방의 메시지를 시간 순서대로 조회
    Page<ChatMessage> findByChatRoomRoomIdOrderByTimestampAsc(String roomId, Pageable pageable);

    // 특정 채팅방에서 특정 수신자에게 보내진 모든 메시지를 시간 순서대로 조회
    List<ChatMessage> findByChatRoomRoomIdAndRecipientIdOrderByTimestampAsc(String roomId, String recipientId);

    // 주어진 채팅방 ID에 대한 마지막 메시지 조회 쿼리
    ChatMessage findFirstByChatRoomIdOrderByIdDesc(String roomId);

    Page<ChatMessage> findByChatRoomIdAndTimestampBefore(String roomId, LocalDateTime timestamp, Pageable pageable);

    Page<ChatMessage> findByChatRoomIdAndRecipientIdAndTimestampBefore(String roomId, String recipientId, LocalDateTime timestamp, Pageable pageable);
}
