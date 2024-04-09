package com.github.devsns.domain.chat.repository;

import com.github.devsns.domain.chat.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    // 특정 채팅방에서 특정 수신자에게 보내진 모든 메시지를 시간 순서대로 조회
    List<ChatMessage> findByRoomIdAndRecipientIdOrderByTimestampAsc(String roomId, String recipientId);


    // 주어진 채팅방 ID에 대한 마지막 메시지 조회 쿼리
    @Query(value = "SELECT * FROM chat_message WHERE room_id = :roomId ORDER BY timestamp DESC LIMIT 1", nativeQuery = true)
    ChatMessage findLastMessageByRoomId(String roomId);


}
