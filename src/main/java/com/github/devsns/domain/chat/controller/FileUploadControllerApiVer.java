package com.github.devsns.domain.chat.controller;

import com.github.devsns.domain.chat.entity.ChatMessage;
import com.github.devsns.domain.chat.service.ChatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
@Tag(name="파일 업로드 api",description = "채팅용 파일 업로드시 진행되는 api")
public class FileUploadControllerApiVer {
    private final ChatService chatService;

    @Operation(summary ="채팅방에 파일을 업로드시")
    @PostMapping("/chat/{roomId}/uploadFile")
    public ChatMessage uploadFileAndSendMessage(@PathVariable String roomId,
                                                @RequestParam("senderId") String senderId,
                                                @RequestParam("file") MultipartFile file) {
        // 파일을 저장하고 파일의 메타데이터를 포함한 채팅 메시지 생성
        ChatMessage fileMessage = chatService.saveFileAndCreateMessage(roomId, senderId, file);

        // 생성된 메시지를 웹소켓을 통해 해당 방의 상대방에게 전송( 웹소켓을 통한 로직이라 삭제)
       // chatService.sendMessageToUser(fileMessage.getRecipientId(), "/queue/messages/" + roomId, fileMessage);

        return fileMessage;
    }

}
