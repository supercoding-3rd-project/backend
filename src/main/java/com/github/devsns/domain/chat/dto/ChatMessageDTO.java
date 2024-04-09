package com.github.devsns.domain.chat.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter@Setter
@Builder
public class ChatMessageDTO {
    private Long messageId; // 메시지의 고유 식별자

    @NotEmpty(message = "Room ID cannot be empty") // 룸 아이디는 비면 안됨
    private String roomId;

    @NotEmpty(message = "Sender ID cannot be empty") // 송신자 아이디는 비면 안됨
    private String senderId;

    private String recipientId; // 수신자 아이디

    @NotEmpty(message = "Message content cannot be empty") // 메시지 내용은 비어있으면 안 됨
    private String content;

    @NotNull(message = "Timestamp cannot be null") // 메시지 전송 시간은 null일 수 없음
    private LocalDateTime timestamp;
}

