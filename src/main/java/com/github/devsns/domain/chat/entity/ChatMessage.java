package com.github.devsns.domain.chat.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "chatMessage")
public class ChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String roomId; // 채팅방 ID
    private String senderId; // 발신자 ID
    private String recipientId; // 수신자 ID

    @Column(nullable = false)
    private String content; // 메시지 내용
    private LocalDateTime timestamp; // 메시지 타임스탬프

    private boolean isRead = false; // 메시지 읽음 상태, 기본값은 false (읽지 않음)

    // 파일 전송을 위한 필드
    @Column(nullable = true)
    private String fileName;
    private boolean hasFile = false; // 파일 포함 여부
    private String fileDownloadUri; // 파일 다운로드 URI

}
