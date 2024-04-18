package com.github.devsns.domain.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoomResponse {
    private String roomId;                // 채팅방 ID
    private String recipientUsername;     // 수신자 이름
    private String senderUsername;        // 발신자 이름
    private String lastMessage;           // 마지막 메시지 내용
    private LocalDateTime lastMessageTime;// 마지막 메시지의 시간
    private boolean isNew; // 새로운 채팅방인지 여부
    // 모든 필드를 초기화하는 생성자
    public RoomResponse(String roomId, String recipientUsername, String lastMessage, LocalDateTime lastMessageTime, boolean isNew) {
        this.roomId = roomId;
        this.recipientUsername = recipientUsername;
        this.lastMessage = lastMessage;
        this.lastMessageTime = lastMessageTime;
        this.isNew = isNew;
    }

}