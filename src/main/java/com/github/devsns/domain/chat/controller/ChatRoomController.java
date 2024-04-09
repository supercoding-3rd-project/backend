package com.github.devsns.domain.chat.controller;

import com.github.devsns.domain.chat.dto.RoomResponse;
import com.github.devsns.domain.chat.service.RoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
public class ChatRoomController {

    private final RoomService roomService;

    // 클라이언트가 특정 사용자와의 채팅방을 생성 또는 조회할 때 호출되는 메서드
    @MessageMapping("/getOrCreateRoom/{recipientId}")
    @SendTo("/topic/roomCreated")
    public RoomResponse getOrCreateRoom(@DestinationVariable String recipientId, @AuthenticationPrincipal UserDetails userDetails) {
        String senderId = userDetails.getUsername(); // UserDetails 인터페이스의 getUsername() 메서드를 사용하여 발신자 ID를 획득
        log.info("채팅방 생성 또는 조회 시작: 발신자={}, 수신자={}", senderId, recipientId);
        RoomResponse roomResponse = roomService.getOrCreateRoom(senderId, recipientId);
        log.info("채팅방 생성 또는 조회 완료: 방 ID={}", roomResponse.getRoomId());
        return roomResponse;
    }


    // 클라이언트가 참여하고 있는 모든 채팅방의 목록을 조회할 때 호출되는 메서드
    @MessageMapping("/getUserRooms/{userId}")
    @SendTo("/topic/userRooms")
    public List<RoomResponse> getUserRooms(@DestinationVariable String userId) {
        // 사용자가 포함된 모든 채팅방의 정보를 조회하여 반환(각 채팅방 정보에는 마지막 메시지와 수신자 정보 등이 포함될 수 있음)
        log.info("사용자 채팅방 목록 조회 시작: 사용자 ID={}", userId);
        List<RoomResponse> userRooms = roomService.getUserRooms(userId);
        log.info("사용자 채팅방 목록 조회 완료: 사용자 ID={}, 채팅방 개수={}", userId, userRooms.size());
        return userRooms;
    }


}
