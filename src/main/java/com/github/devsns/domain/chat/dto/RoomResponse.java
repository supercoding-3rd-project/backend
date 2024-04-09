package com.github.devsns.domain.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoomResponse {
    private String roomId;      // 채팅방 ID
    private String recipientId; // 수신자 ID
    private String lastMessage; // 마지막 메시지 내용
    private LocalDateTime lastMessageTimestamp; // 마지막 메시지의 시간
}