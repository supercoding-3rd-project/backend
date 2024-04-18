//package com.github.devsns.domain.chat.websocket;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.github.devsns.domain.chat.entity.ChatMessage;
//import com.github.devsns.domain.chat.service.ChatService;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Component;
//import org.springframework.web.socket.TextMessage;
//import org.springframework.web.socket.WebSocketSession;
//import org.springframework.web.socket.handler.TextWebSocketHandler;
//
//@RequiredArgsConstructor
//@Component
//@Slf4j
//public class ChatWebSocketHandler extends TextWebSocketHandler {
//
//    private final ChatService chatService;
//
//    @Override
//    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
//        // 클라이언트로부터 수신된 메시지
//        String payload = message.getPayload();
//
//        // 메시지 로깅
//        log.info("Received message from session {}: {}", session.getId(), payload);
//
//        // 클라이언트로부터 수신된 메시지를 ChatMessage 객체로 변환
//        ChatMessage chatMessage = convertToChatMessage(payload);
//
//        if (chatMessage == null) {
//            // 메시지 파싱에 실패한 경우
//            log.error("Failed to parse message from session {}: {}", session.getId(), payload);
//            return;
//        }
//
//        // 채팅 메시지 저장
//        try {
//            chatService.saveChatMessage(chatMessage);
//            log.info("Saved chat message from session {}", session.getId());
//        } catch (Exception e) {
//            // 채팅 메시지 저장 중 오류 발생 시
//            log.error("Failed to save chat message from session {}: {}", session.getId(), e.getMessage());
//            // 예외를 다시 던져서 상위 레벨에서 처리할 수 있도록 함
//            throw new RuntimeException("Failed to save chat message", e);
//        }
//
//        // 클라이언트에게 수신된 메시지 다시 전송
//        try {
//            session.sendMessage(new TextMessage("Received: " + payload));
//            log.info("Sent acknowledgment to session {}", session.getId());
//        } catch (Exception e) {
//            // 클라이언트에게 메시지를 전송하는 동안 오류 발생 시
//            log.error("Failed to send acknowledgment to session {}: {}", session.getId(), e.getMessage());
//            // 예외를 다시 던져서 상위 레벨에서 처리할 수 있도록 함
//            throw new RuntimeException("Failed to send acknowledgment", e);
//        }
//    }
//
//
//    private ChatMessage convertToChatMessage(String payload) {
//        ObjectMapper objectMapper = new ObjectMapper();
//        try {
//            // JSON 문자열을 ChatMessage 객체로 변환
//            return objectMapper.readValue(payload, ChatMessage.class);
//        } catch (JsonProcessingException e) {
//            // JSON 파싱 중 오류 발생 시
//            log.error("Error parsing JSON: {}", e.getMessage());
//            // 예외 처리 또는 기본값 설정 등을 수행
//            return null;
//        }
//    }
//
//}