package com.github.devsns.domain.chat.controller;

import com.github.devsns.domain.chat.entity.ChatMessage;
import com.github.devsns.domain.chat.service.ChatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;



@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "채팅용 api", description = "웹소켓방식 아닌 채팅용 api")
public class ChatControllerApiVer {
    private final ChatService chatService;

    // 채팅 메시지와 선택적으로 파일을 처리하는 REST API 엔드포인트로 변경
    @Operation(summary = "채팅 메시지와 파일을 처리하는 REST API 엔드포인트")
    @PostMapping("/chat/{roomId}")
    public ResponseEntity<?> handleChatMessage(@DestinationVariable String roomId,
                                               @Parameter(description = "채팅 메시지") ChatMessage message,
                                               @RequestParam(value = "file", required = false) MultipartFile file) {
        // 파일 처리를 포함한 메시지 처리 로직 호출
        chatService.processMessage(roomId, message, file);
        log.info("방 '{}'에서 메시지 처리 완료", roomId);
        return ResponseEntity.ok().build();
    }


    // 메시지를 '읽음' 상태로 표시하는 API 엔드포인트
    @Operation(summary ="해당 메세지 읽음처리 여부 api")
    @PostMapping("/messages/{messageId}/read")
    public ResponseEntity<?> markMessageAsRead(@PathVariable Long messageId) {
        // 메시지 ID에 해당하는 메시지를 '읽음'으로 표시
        chatService.markMessageAsRead(messageId);
        log.info("메시지 ID '{}'를 읽음으로 표시", messageId);
        return ResponseEntity.ok().build();
    }

}