package com.github.devsns.domain.chat.controller;


import com.github.devsns.domain.chat.entity.ChatMessage;
import com.github.devsns.domain.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;



@Controller
@Slf4j
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    // 채팅 메시지와 선택적으로 파일을 처리하는 WebSocket 엔드포인트
    @MessageMapping("/chat/{roomId}")
    public void handleChatMessage(@DestinationVariable String roomId, ChatMessage message, MultipartFile file) {
        // 파일 처리를 포함한 메시지 처리 로직 호출
        chatService.processMessage(roomId, message, file);
        log.info("방 '{}'에서 메시지 처리 완료", roomId);
    }


    // 메시지를 '읽음' 상태로 표시하는 API 엔드포인트
    @PostMapping("/messages/{messageId}/read")
    public ResponseEntity<?> markMessageAsRead(@PathVariable Long messageId) {
        // 메시지 ID에 해당하는 메시지를 '읽음'으로 표시
        chatService.markMessageAsRead(messageId);
        log.info("메시지 ID '{}'를 읽음으로 표시", messageId);
        return ResponseEntity.ok().build();
    }

}