package com.github.devsns.domain.chat.controller;

import com.github.devsns.domain.chat.dto.RoomResponse;
import com.github.devsns.domain.chat.entity.ChatMessage;
import com.github.devsns.domain.chat.service.ChatService;
import com.github.devsns.domain.chat.service.RoomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;


@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "채팅용 api", description = "웹소켓방식 아닌 채팅용 api")
public class ChatControllerApiVer {
    private final ChatService chatService;
    private final RoomService roomService;

    // 채팅 메시지와 선택적으로 파일을 처리하는 REST API 엔드포인트로 변경
    @Operation(summary = "메시지 전송 (파일 포함 가능)")
    @PostMapping("/{roomId}/send")
    public ResponseEntity<?> handleChatMessage(
            @PathVariable String roomId,
            @RequestParam("senderId") String senderId,
            @RequestParam("recipientId") String recipientId,
            @RequestParam(value = "content", required = false) String content,
            @RequestParam(value = "file", required = false) MultipartFile file) {
        // 파일 처리를 포함한 메시지 처리 로직 호출
        chatService.processMessage(roomId, senderId, recipientId, content, file);
        log.info("방 '{}'에서 메시지 처리 완료", roomId);
        return ResponseEntity.ok().build();
    }

    //채팅방의 메시지 아력 조회
    @Operation(summary = "채팅방 메시지 내역 조회")
    @GetMapping("/{roomId}/history")
    public ResponseEntity<List<ChatMessage>> getHistory(
            @PathVariable String roomId,
            @RequestParam(required = false) String recipientId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime lastTimestamp,
            @RequestParam(defaultValue = "20") int size) {
        log.info("방 '{}' 내역 조회 완료", roomId);
        return ResponseEntity.ok(chatService.getChatHistory(roomId, recipientId,lastTimestamp, size));

    }


    // 메시지를 '읽음' 상태로 표시하는 API 엔드포인트
    @Operation(summary ="해당 메세지 읽음처리 여부 api")
    @PostMapping("/messages/{messageId}/read")
    public ResponseEntity<?> markMessageAsRead(
            @PathVariable Long messageId,
            @RequestParam("recipientId") String recipientId) {  // recipientId를 RequestParam으로 받음
        boolean result = chatService.markMessageAsRead(messageId, recipientId);
        // 메시지 ID에 해당하는 메시지를 '읽음'으로 표시
        if (result) {
            log.info("메시지 ID '{}'와 수신자 ID '{}'를 읽음으로 표시", messageId, recipientId);
            return ResponseEntity.ok().build(); // 응답 성공
        } else {
            log.info("메시지 ID '{}'와 수신자 ID '{}'를 읽음으로 표시 실패. 메시지가 존재하지 않거나 접근 권한이 없습니다.", messageId, recipientId);
            return ResponseEntity.notFound().build(); // 메시지가 존재하지 않거나 오류 발생
        }
    }

    //사용자 채팅방 목록 조회
    @Operation(summary = "사용자의 채팅방 목록 조회")
    @GetMapping("/chat/rooms/{userId}")
    public ResponseEntity<List<RoomResponse>> getUserRooms(@PathVariable String userId) {
        log.info("사용자 '{}'의 채팅방 목록 조회", userId);
        return ResponseEntity.ok(roomService.getUserRooms(userId));
    }

}