package com.github.devsns.domain.chat.controller;


import com.github.devsns.domain.chat.dto.RoomResponse;
import com.github.devsns.domain.chat.service.RoomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController // REST API 컨트롤러임을 명시
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/rooms")
@Tag(name = "채팅룸 api", description = "웹소켓없이 채팅방 관련한 api")
public class ChatRoomControllerApiVer {
    private final RoomService roomService;

    // 클라이언트가 특정 사용자와의 채팅방을 생성 또는 조회할 때 호출되는 메서드 REST API로 변환
    @Operation(summary ="특정 유저와의 채팅방 생성/조회시")
    @GetMapping("/get-or-create")
    public ResponseEntity<RoomResponse> getOrCreateRoom(
            @RequestParam String recipientId, Authentication authentication) {
        String senderId = authentication.getName(); // 인증된 사용자 ID 추출
        RoomResponse roomResponse = roomService.getOrCreateRoom(senderId, recipientId);
        if (roomResponse.getRoomId() != null) {
            log.info("방 '{}'을 반환합니다. 새 방 여부: {}", roomResponse.getRoomId(), roomResponse.isNew());
            return ResponseEntity.ok(roomResponse);
        } else {
            log.warn("방 생성 또는 조회에 실패했습니다.");
            return ResponseEntity.badRequest().build();
        }
    }


    // 클라이언트가 참여하고 있는 모든 채팅방의 목록을 조회할 때 호출되는 메서드 REST API로 변환
    @Operation(summary ="유저의 참여하고 있는 모든 채팅방 목록 조회")
    @GetMapping("user/{userId}")
    public ResponseEntity<List<RoomResponse>> getUserRooms(@PathVariable String userId) {
        // 사용자가 포함된 모든 채팅방의 정보를 조회하여 반환(각 채팅방 정보에는 마지막 메시지와 수신자 정보 등이 포함될 수 있음)
        log.info("사용자 채팅방 목록 조회 시작: 사용자 ID={}", userId);
        List<RoomResponse> userRooms = roomService.getUserRooms(userId);
        if (!userRooms.isEmpty()) {
            log.info("채팅방 목록 조회 결과: 사용자 '{}'가 {}개의 채팅방에 참여 중입니다.", userId, userRooms.size());
            return ResponseEntity.ok(userRooms);
        } else {
            log.info("사용자 '{}'의 채팅방 목록 조회z: 채팅방이 없습니다.", userId);
            return ResponseEntity.noContent().build();
        }
    }

    //채팅방 닉네임 검색
    @Operation(summary = "특정 닉네임을 기반으로 채팅방 검색")
    @GetMapping("/search")
    public ResponseEntity<List<RoomResponse>> searchRoomsByNickname(
            @RequestParam String keywordUsername, Authentication authentication)  {
        log.info("닉네임 '{}'을 포함하는 채팅방을 검색합니다.", keywordUsername);

        String currentUserId = authentication.getName(); // 인증된 사용자의 ID를 추출
        List<RoomResponse> rooms = roomService.searchChatRoomsByNickname(currentUserId, keywordUsername); // currentUserId 추가

        if (!rooms.isEmpty()) {
            log.info("검색결과: '{}'를 포함하는 채팅방이 {}개 찾아졌습니다.", keywordUsername, rooms.size());
            return ResponseEntity.ok(rooms);
        } else {
            log.info("검색결과: '{}'를 포함하는 채팅방이 없습니다.", keywordUsername);
            return ResponseEntity.noContent().build();
        }
    }



}
