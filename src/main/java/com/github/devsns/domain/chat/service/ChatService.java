package com.github.devsns.domain.chat.service;

import com.github.devsns.domain.chat.entity.ChatMessage;
import com.github.devsns.domain.chat.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatService {

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatMessageRepository chatMessageRepository;
    private final FileStorageService fileStorageService;

    /// 채팅 메시지와 선택적으로 파일을 처리하는 메서드
    public void processMessage(String roomId, ChatMessage message, MultipartFile file) {
        if (file != null && !file.isEmpty()) {
            // 파일 업로드 처리
            handleFileUpload(message, file);
        }

        message.setRoomId(roomId);
        message.setTimestamp(LocalDateTime.now()); // 메시지 타임스탬프 설정
        chatMessageRepository.save(message); // 메시지 저장
        log.debug("메시지 저장됨: {}", message);

        // 메시지 수신자에게 메시지 전송
        sendMessageToUser(message.getRecipientId(), "/queue/messages/" + roomId, message);
        log.info("방 ID '{}'에서 '{}'에게 메시지 전송됨.", roomId, message.getRecipientId());
    }

    // 파일 업로드를 처리하고, 채팅 메시지에 파일 정보를 추가하는 메서드
    private void handleFileUpload(ChatMessage message, MultipartFile file) {
        try {
            String fileDownloadUri = fileStorageService.storeFile(file);
            message.setHasFile(true);
            message.setFileName(file.getOriginalFilename());
            message.setFileDownloadUri(fileDownloadUri);
            log.info("파일 '{}' 저장됨, 다운로드 URI: {}", file.getOriginalFilename(), fileDownloadUri);
        } catch (IOException e) {
            log.error("파일 '{}' 저장 중 오류 발생: {}", file.getOriginalFilename(), e.getMessage());
            // 필요한 경우 사용자에게 오류 메시지를 반환
            throw new RuntimeException("파일 저장 실패: " + file.getOriginalFilename());
        }
    }

    // 메시지를 읽음으로 표시하는 메서드
    public void markMessageAsRead(Long messageId) {
        chatMessageRepository.findById(messageId).ifPresent(message -> {
            if (!message.isRead()) {
                message.setRead(true);
                chatMessageRepository.save(message);
                log.info("메시지 ID '{}'가 읽음으로 표시되었습니다.", messageId);
            }
        });
    }

    // 채팅 내역 조회
    public List<ChatMessage> getChatHistory(String roomId, String recipientId) {
        log.debug("채팅 내역 조회: 방 ID={}, 수신자 ID={}", roomId, recipientId);
        List<ChatMessage> chatHistory = chatMessageRepository.findByRoomIdAndRecipientIdOrderByTimestampAsc(roomId, recipientId);
        log.debug("채팅 내역 조회 결과: 방 ID={}, 메시지 개수={}", roomId, chatHistory.size());
        return chatHistory;
    }

    // 채팅 메시지 저장
    public void saveChatMessage(ChatMessage chatMessage) {
        log.debug("채팅 메시지 저장: {}", chatMessage);
        chatMessageRepository.save(chatMessage);
        log.debug("채팅 메시지 저장 완료");
    }

   // 채팅방 업데이트 메시지 전송
    public void sendRoomUpdateMessage(ChatMessage message) {
        log.info("채팅방 업데이트 메시지를 전송합니다. 방 ID: {}", message.getRoomId());
        send("/topic/messages", message);
        log.info("채팅방 업데이트 메시지 전송이 완료되었습니다.");
    }

    // 메시지를 사용자에게 전송
    public void sendMessageToUser(String recipientId, String destination, ChatMessage message) {
        try {
            log.debug("사용자에게 메시지 전송: 수신자 ID={}, 목적지={}, 메시지={}", recipientId, destination, message);
            messagingTemplate.convertAndSendToUser(recipientId, destination, message);
            log.info("메시지 전송 성공: 수신자 ID={}", recipientId);
        } catch (Exception e) {
            log.error("메시지 전송 실패: 수신자 ID={}. 오류: {}", recipientId, e.getMessage());
            throw new MessageSendingException("메시지 전송 실패", e);
        }
    }

    // 주어진 채팅방 ID에 대한 마지막 메시지를 조회하는 메서드
    public ChatMessage getLastMessage(String roomId) {
        log.debug("마지막 메시지 조회: 방 ID={}", roomId);
        ChatMessage lastMessage = chatMessageRepository.findLastMessageByRoomId(roomId);
        log.debug("마지막 메시지 조회 결과: 방 ID={}, 메시지={}", roomId, lastMessage);
        return lastMessage;
    }

    // 메시지 전송 예외 처리 클래스
    public static class MessageSendingException extends RuntimeException {
        public MessageSendingException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    // 메시지 전송
    private void send(String destination, ChatMessage message) {
        messagingTemplate.convertAndSend(destination, message);
    }

    //파일을 저장 &채팅 메시지 생성
    public ChatMessage saveFileAndCreateMessage(String roomId, String senderId, MultipartFile file) {
        String fileName = file.getOriginalFilename();
        String filePath;
        try {
            filePath = fileStorageService.storeFile(file); // 파일 저장
        } catch (IOException e) {
            log.error("파일 '{}' 저장 중 오류 발생: {}", fileName, e.getMessage());
            throw new RuntimeException("파일 저장 실패: " + fileName, e); // 혹은 다른 예외 처리 방법을 사용
        }

        ChatMessage fileMessage = new ChatMessage();
        fileMessage.setRoomId(roomId);
        fileMessage.setSenderId(senderId);
        fileMessage.setFileName(fileName);
        fileMessage.setFileDownloadUri(filePath); // 파일 다운로드 URI 설정
        fileMessage.setHasFile(true);
        fileMessage.setTimestamp(LocalDateTime.now());

        chatMessageRepository.save(fileMessage); // 메시지 저장

        return fileMessage;
    }

    public void broadcastMessage(ChatMessage message) {
        messagingTemplate.convertAndSend("/topic/chat/" + message.getRoomId(), message);
    }


}


