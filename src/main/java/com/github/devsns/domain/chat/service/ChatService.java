package com.github.devsns.domain.chat.service;

import com.github.devsns.domain.chat.entity.ChatMessage;
import com.github.devsns.domain.chat.entity.ChatRoom;
import com.github.devsns.domain.chat.repository.ChatMessageRepository;
import com.github.devsns.domain.chat.repository.ChatRoomRepository;
import com.github.devsns.domain.chat.repository.MessageReadReceiptRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
@Slf4j
public class ChatService {

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatMessageRepository chatMessageRepository;
    private final FileStorageService fileStorageService;
    private final ChatRoomRepository chatRoomRepository;
    private final MessageReadReceiptRepository readReceiptRepository;

    /// 채팅 메시지와 파일을 처리
    public void processMessage(String roomId, String senderId, String recipientId, String content, MultipartFile file) throws IllegalArgumentException {
        ChatRoom chatRoom = chatRoomRepository.findByRoomId(roomId)
                .orElseThrow(() -> new IllegalArgumentException("채팅방을 찾을 수 없습니다. roomId: " + roomId));

        ChatMessage message = new ChatMessage(senderId, recipientId, content, LocalDateTime.now(), chatRoom);
        if (file != null && !file.isEmpty()) {
            handleFileUpload(message, file);
        }

        chatMessageRepository.save(message);
        log.info("방 '{}'에서 '{}'에게 메시지 '{}'을(를) 전송했습니다.", roomId, recipientId, content);

        sendMessageToUser(recipientId, "/queue/messages/" + roomId, message);
        markMessageAsRead(message.getId(), recipientId);
    }


    // 파일 업로드 처리
    private void handleFileUpload(ChatMessage message, MultipartFile file) {
        try {
            String fileDownloadUri = fileStorageService.storeFile(file);
            message.setHasFile(true);
            message.setFileName(file.getOriginalFilename());
            message.setFileDownloadUri(fileDownloadUri);
            log.info("파일 '{}' 저장됨, 다운로드 URI: {}", file.getOriginalFilename(), fileDownloadUri);
        } catch (IOException e) {
            log.error("파일 '{}' 저장 중 오류 발생: {}", file.getOriginalFilename(), e.getMessage());
            throw new RuntimeException("파일 저장 실패: " + file.getOriginalFilename(), e);
        }
    }
    // 메시지를 읽음처리
    /**
     * 지정된 메시지를 읽음 상태로 업데이트합니다.
     *
     * @param messageId   읽을 메시지의 ID
     * @param recipientId 메시지를 읽은 사용자의 ID
     * @return 메시지 읽기가 성공적으로 처리되었는지 여부를 반환
     */
    public boolean markMessageAsRead(Long messageId, String recipientId) {
        Optional<ChatMessage> messageOpt = chatMessageRepository.findById(messageId);

        if (messageOpt.isPresent()) {
            ChatMessage message = messageOpt.get();
            if (!message.isRead() && recipientId.equals(message.getRecipientId())) {
                message.setRead(true);
                chatMessageRepository.save(message);
                log.info("메시지 ID '{}'을(를) 읽음으로 표시했습니다.", messageId);  // 성공 로그
                return true;
            } else {
                if (!recipientId.equals(message.getRecipientId())) {
                    log.info("메시지 ID '{}'의 수신자 ID가 일치하지 않습니다.", messageId); // 수신자 불일치 로그
                }
                if (message.isRead()) {
                    log.info("메시지 ID '{}'는 이미 읽음 상태입니다.", messageId); // 이미 읽은 상태 로그
                }
            }
        } else {
            log.error("메시지 ID '{}'를 찾을 수 없습니다.", messageId); // 찾을 수 없음 로그
        }
        return false;
    }

    //  채팅 내역 조회: 커서 기반 페이지네이션
    public List<ChatMessage> getChatHistory(String roomId, String recipientId, LocalDateTime lastTimestamp, int size) {
        Pageable pageable = PageRequest.of(0, size, Sort.by(Sort.Direction.DESC, "timestamp"));

        Page<ChatMessage> page;
        if (recipientId == null || recipientId.isEmpty()) {
            page = chatMessageRepository.findByChatRoomIdAndTimestampBefore(roomId, lastTimestamp, pageable);
        } else {
            page = chatMessageRepository.findByChatRoomIdAndRecipientIdAndTimestampBefore(roomId, recipientId, lastTimestamp, pageable);
        }

        log.info("채팅 내역 조회 완료: 방 ID={}, 메시지 개수={}", roomId, page.getContent().size());
        return page.getContent();


    }

    // 채팅 메시지 저장
    public void saveChatMessage(ChatMessage chatMessage) {
        log.debug("채팅 메시지 저장: {}", chatMessage);
        chatMessageRepository.save(chatMessage);
        log.debug("채팅 메시지 저장 완료");
    }

   // 채팅방 업데이트 메시지 전송
    public void sendRoomUpdateMessage(ChatMessage message) {
        log.info("채팅방 업데이트 메시지를 전송합니다. 방 ID: {}", message.getChatRoom().getRoomId());
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
        ChatMessage lastMessage = chatMessageRepository.findFirstByChatRoomIdOrderByIdDesc(roomId);
        log.debug("마지막 메시지 조회 결과: 방 ID={}, 메시지={}", roomId, lastMessage.getTimestamp());
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
    public ChatMessage saveFileAndCreateMessage(String roomId, String senderId, String recipientId, String content, MultipartFile file) {
        String fileName = file.getOriginalFilename();
        String filePath;
        try {
            filePath = fileStorageService.storeFile(file); // 파일 저장
        } catch (IOException e) {
            log.error("파일 '{}' 저장 중 오류 발생: {}", fileName, e.getMessage());
            throw new RuntimeException("파일 저장 실패: " + fileName, e); // 혹은 다른 예외 처리 방법을 사용
        }

        // roomId를 사용하여 ChatRoom 객체를 찾습니다.
        Optional<ChatRoom> chatRoomOptional = chatRoomRepository.findByRoomId(roomId);
        if (!chatRoomOptional.isPresent()) {
            log.error("채팅방 '{}'를 찾을 수 없습니다.", roomId);
            return null; // 적절한 예외 처리를 고려해야 할 수도 있습니다.
        }
        ChatRoom chatRoom = chatRoomOptional.get();


        ChatMessage fileMessage = new ChatMessage(senderId, recipientId, content, LocalDateTime.now(), chatRoom);
        fileMessage.setHasFile(true);
        fileMessage.setFileName(fileName);
        fileMessage.setFileDownloadUri(filePath);

        chatMessageRepository.save(fileMessage); // 메시지 저장

        return fileMessage;
    }

    public void broadcastMessage(ChatMessage message) {
        messagingTemplate.convertAndSend("/topic/chat/" + message.getChatRoom().getRoomId(), message);
    }


}


