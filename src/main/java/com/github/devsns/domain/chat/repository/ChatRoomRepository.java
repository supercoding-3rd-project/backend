package com.github.devsns.domain.chat.repository;

import com.github.devsns.domain.chat.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long>, ChatRoomRepositoryCustom {
   // 특정 참여자 조합으로 채팅방을 조회
    Optional<ChatRoom> findRoomByParticipants(String participant1, String participant2);

    // roomId로 채팅방 찾기
    Optional<ChatRoom> findByRoomId(String roomId);


    // participant1 또는 participant2의 이름을 포함하는 채팅방을 조회
    List<ChatRoom> findRoomsByParticipant1_UsernameContainingOrParticipant2_UsernameContaining(String username1, String username2);








}
