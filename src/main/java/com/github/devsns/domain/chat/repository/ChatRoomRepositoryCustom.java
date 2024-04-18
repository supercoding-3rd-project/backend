package com.github.devsns.domain.chat.repository;

import com.github.devsns.domain.chat.entity.ChatRoom;
import com.github.devsns.domain.user.entitiy.UserEntity;

import java.util.List;

public interface ChatRoomRepositoryCustom {
    //내 채팅방 목록에 상대방 name 으로 조회
    List<ChatRoom> findRoomsByRecipientNameContaining(String username, Long participantId);
    List<ChatRoom> findChatRoomsByParticipant(UserEntity participant);
}
