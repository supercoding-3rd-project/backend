package com.github.devsns.domain.chat.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "chat_messages")
public class ChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne
    @JoinColumn(name = "chat_room_id", referencedColumnName = "id")
    private ChatRoom chatRoom; // 채팅방 ID

    @Column(name = "sender_id", nullable = false, length = 255)
    private String senderId; // 발신자 ID
    @Column(name = "recipient_id", nullable = false, length = 255)
    private String recipientId; // 수신자 ID

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content; // 메시지 내용
    @Column(nullable = false)
    @Convert(converter = LocalDateTimeConverter.class)
    private LocalDateTime timestamp; // 메시지 타임스탬프

    @Column(nullable = false) // isRead는 nullable=false로 지정
    private boolean isRead = false; // 메시지 읽음 상태, 기본값은 false (읽지 않음)

    // 파일 전송을 위한 필드
    @Column
    private String fileName;
    @Column
    private boolean hasFile = false; // 파일 포함 여부
   @Column(nullable = true)
   private String fileDownloadUri; // 파일 다운로드 URI

}
