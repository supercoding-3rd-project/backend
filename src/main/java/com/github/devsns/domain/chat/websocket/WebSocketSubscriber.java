//package com.github.devsns.domain.chat.websocket;
//
//import org.springframework.messaging.handler.annotation.DestinationVariable;
//import org.springframework.messaging.handler.annotation.MessageMapping;
//import org.springframework.messaging.handler.annotation.SendTo;
//import org.springframework.stereotype.Controller;
//
//@Controller
//public class WebSocketSubscriber {
//    @MessageMapping("/subscribe/{roomId}")
//    @SendTo("/topic/messages/{roomId}")
//    public String subscribeToRoom(@DestinationVariable String roomId) {
//        // 클라이언트가 해당 룸에 구독하도록 메시지 전송
//        return "Subscribed to room " + roomId;
//    }
//}
