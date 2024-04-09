package com.github.devsns.domain.chat.websocket;

import com.github.devsns.domain.chat.entity.ChatMessage;
import com.github.devsns.domain.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.multipart.MultipartFile;

@Controller
@Slf4j
@RequiredArgsConstructor
public class ChatWebSocketController {

    private final ChatService chatService;

    @MessageMapping("/chat")
    public void processChatMessage(ChatMessage message, MultipartFile file) {
        chatService.processMessage(message.getRoomId(), message, file);
    }
}

