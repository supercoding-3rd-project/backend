package com.github.devsns.domain.chat.repository;

import com.github.devsns.domain.chat.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long>,ChatRoomRepositoryCustom  {

//    List<ChatRoom> findRoomsByRecipientNameContaining(String senderId, String recipientId);

//    List<ChatRoom> findRoomsBySenderNameContaining(String keyword);

    // roomId로 ChatRoom 찾기
    Optional<ChatRoom> findByRoomId(String roomId);
}
