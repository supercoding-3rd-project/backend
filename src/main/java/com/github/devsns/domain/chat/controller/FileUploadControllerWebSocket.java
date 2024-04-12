//package com.github.devsns.domain.chat.controller;
//
//import com.github.devsns.domain.chat.entity.ChatMessage;
//import com.github.devsns.domain.chat.service.ChatService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.messaging.handler.annotation.DestinationVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.multipart.MultipartFile;
//
//@RequiredArgsConstructor
//@RestController
//@RequestMapping("/api")
//public class FileUploadControllerWebSocket {
//    private final ChatService chatService;
//
//    @PostMapping("/chat/{roomId}/uploadFile")
//    public ChatMessage uploadFileAndSendMessage(@DestinationVariable String roomId, @RequestParam("senderId") String senderId, @RequestParam("file") MultipartFile file) {
//        // 파일을 저장하고 파일의 메타데이터를 포함한 채팅 메시지 생성
//        ChatMessage fileMessage = chatService.saveFileAndCreateMessage(roomId, senderId, file);
//
//        // 생성된 메시지를 웹소켓을 통해 해당 방의 상대방에게 전송
//        chatService.sendMessageToUser(fileMessage.getRecipientId(), "/queue/messages/" + roomId, fileMessage);
//
//        return fileMessage;
//    }
//}
