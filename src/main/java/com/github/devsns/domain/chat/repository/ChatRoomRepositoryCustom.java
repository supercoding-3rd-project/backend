package com.github.devsns.domain.chat.repository;

import com.github.devsns.domain.chat.entity.ChatRoom;

import java.util.List;
import java.util.Optional;

public interface ChatRoomRepositoryCustom {
    // 특정 사용자가 참여하는 모든 채팅방을 조회하는 메서드
    List<ChatRoom> findCustomRoomsByParticipantId(Long userId);

    // 두 사용자가 참여하는 채팅방을 조회하는 메서드
    Optional<ChatRoom> findRoomByParticipants(String participant1, String participant2);


    // 다른 사용자와의 채팅방을 조회하는 메서드
    List<ChatRoom> findRoomsByCurrentUserIdAndOtherUsername(Long currentUserId, String otherUsername);


}
