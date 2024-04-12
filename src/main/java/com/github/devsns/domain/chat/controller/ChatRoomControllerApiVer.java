package com.github.devsns.domain.chat.controller;


import com.github.devsns.domain.chat.dto.RoomResponse;
import com.github.devsns.domain.chat.service.RoomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController // REST API 컨트롤러임을 명시
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "채팅룸 api", description = "웹소켓없이 채팅방 관련한 api")
public class ChatRoomControllerApiVer {
    private final RoomService roomService;

    // 클라이언트가 특정 사용자와의 채팅방을 생성 또는 조회할 때 호출되는 메서드 REST API로 변환
    @Operation(summary ="특정 유저와의 채팅방 생성/조회시")
    @PostMapping("/rooms/{recipientId}")
    public RoomResponse createOrGetRoom(@PathVariable String recipientId, @AuthenticationPrincipal UserDetails userDetails) {
        String senderId = userDetails.getUsername(); // 발신자 ID 획득
        log.info("채팅방 생성 또는 조회 시작: 발신자={}, 수신자={}", senderId, recipientId);
        RoomResponse roomResponse = roomService.getOrCreateRoom(senderId, recipientId);
        log.info("채팅방 생성 또는 조회 완료: 방 ID={}", roomResponse.getRoomId());
        return roomResponse; // RoomResponse 객체를 직접 반환하여 응답 본문에 포함
    }


    // 클라이언트가 참여하고 있는 모든 채팅방의 목록을 조회할 때 호출되는 메서드 REST API로 변환
    @Operation(summary ="유저의 참여하고 있는 모든 채팅방 목록 조회")
    @GetMapping("/rooms/user/{userId}")
    public List<RoomResponse> getUserRooms(@PathVariable String userId) {
        // 사용자가 포함된 모든 채팅방의 정보를 조회하여 반환(각 채팅방 정보에는 마지막 메시지와 수신자 정보 등이 포함될 수 있음)
        log.info("사용자 채팅방 목록 조회 시작: 사용자 ID={}", userId);
        List<RoomResponse> userRooms = roomService.getUserRooms(userId);
        log.info("사용자 채팅방 목록 조회 완료: 사용자 ID={}, 채팅방 개수={}", userId, userRooms.size());
        return userRooms; // 사용자 채팅방 목록을 직접 반환하여 응답 본문에 포함
    }


}
