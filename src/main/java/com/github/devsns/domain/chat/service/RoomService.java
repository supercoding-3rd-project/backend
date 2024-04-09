package com.github.devsns.domain.chat.service;


import com.github.devsns.domain.chat.dto.RoomDto;
import com.github.devsns.domain.chat.dto.RoomResponse;
import com.github.devsns.domain.chat.entity.ChatMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class RoomService {
    private final Map<String, RoomDto> roomMap = new HashMap<>();
    private final ChatService chatService;

    // 특정 사용자와의 채팅방을 조회 또는 생성하는 메서드
    public RoomResponse getOrCreateRoom(String senderId, String recipientId) {
        log.info("특정 사용자와의 채팅방을 조회 또는 생성합니다.");
        // 특정 사용자와의 채팅방이 이미 존재하는지 확인
        boolean roomExists = isRoomExists(senderId) || isRoomExists(recipientId);
        if (roomExists) {
            // 이미 채팅방이 존재하는 경우 해당 채팅방의 아이디를 반환
            String roomId = getRoomId(senderId);
            if (roomId == null) {
                roomId = getRoomId(recipientId);
            }
            log.info("특정 사용자와의 채팅방이 이미 존재합니다. 채팅방 ID: {}", roomId);
            return RoomResponse.builder().roomId(roomId).build();
        } else {
            // 채팅방이 존재하지 않는 경우 새로운 채팅방을 생성하고 해당 아이디를 반환
            String roomId = generateRoomId(senderId, recipientId);
            log.info("새로운 채팅방을 생성했습니다. 채팅방 ID: {}", roomId);
            return RoomResponse.builder().roomId(roomId).build();
        }
    }
    // 사용자가 포함된 모든 채팅방의 목록을 조회하는 메서드
    public List<RoomResponse> getUserRooms(String userId) {
        log.info("사용자가 포함된 모든 채팅방의 목록을 조회합니다. 사용자 ID: {}", userId);

        // 해당 사용자가 포함된 모든 채팅방의 ID 목록 조회
        List<String> roomIds = roomMap.keySet().stream()
                .filter(roomId -> roomId.contains(userId))
                .toList();

        List<RoomResponse> roomResponses = new ArrayList<>();

        // 각 채팅방의 마지막 메시지와 수신자 정보를 포함하여 RoomResponse 객체 생성
        for (String roomId : roomIds) {
            // 수신자 ID 추출 (채팅방 ID에서 사용자 ID를 제외한 부분)
            String recipientId = roomId.replace(userId, "").replace("_", "");

            // 마지막 메시지 조회 (이 부분은 chatService의 구현에 따라 달라질 수 있음)
            ChatMessage lastMessage = chatService.getLastMessage(roomId);

            RoomResponse roomResponse = RoomResponse.builder()
                    .roomId(roomId)
                    .recipientId(recipientId)
                    .lastMessage(lastMessage.getContent()) // 마지막 메시지의 내용
                    .build();

            roomResponses.add(roomResponse);
        }

        return roomResponses;
    }

    // 유저와 수신자를 구분하지 않고 동일한 룸 아이디 생성
    public String generateRoomId(String senderId, String recipientId) {
        log.info("동일한 룸 아이디를 생성합니다.");
        String roomId = senderId + "_" + recipientId;
        return roomMap.computeIfAbsent(roomId, k -> new RoomDto(UUID.randomUUID().toString())).getId();
    }

    // 특정 유저와의 룸 아이디 존재 여부 확인
    public boolean isRoomExists(String userId) {
        // 해당 userId가 포함된 룸 아이디가 있는지 여부 확인
        log.info("특정 유저와의 채팅방이 존재하는지 확인합니다. 사용자 ID: {}", userId);
        return roomMap.keySet().stream().anyMatch(roomId -> roomId.contains(userId));
    }

    // 특정 유저와의 룸 아이디 반환
    public String getRoomId(String userId) {
        // 해당 userId를 포함하는 룸 아이디 반환
        log.info("특정 유저와의 채팅방 ID를 조회합니다. 사용자 ID: {}", userId);
        return roomMap.keySet().stream().filter(roomId -> roomId.contains(userId)).findFirst().orElse(null);
    }


    // 룸에 세션 추가
    public void addSessionToRoom(String roomId, WebSocketSession session) {
        RoomDto roomDto = roomMap.get(roomId);
        if (roomDto != null) {
            roomDto.addSession(session);
            log.info("룸에 세션을 추가했습니다. 세션 ID: {}, 룸 ID: {}, 총 세션 수: {}", session.getId(), roomId, roomDto.getSessionCount());
        } else {
            log.error("해당 룸이 존재하지 않습니다. 룸 ID: {}", roomId);
        }
    }

    // 룸에서 세션 제거
    public void removeSessionFromRoom(String roomId, WebSocketSession session) {
        RoomDto roomDto = roomMap.get(roomId);
        if (roomDto != null) {
            roomDto.removeSession(session);
            log.info("룸에서 세션을 제거했습니다. 세션 ID: {}, 룸 ID: {}, 총 세션 수: {}", session.getId(), roomId, roomDto.getSessionCount());
            if (roomDto.isEmpty()) {
                roomMap.remove(roomId);
                log.info("룸이 비어있어 삭제했습니다. 룸 ID: {}", roomId);
            }
        } else {
            log.error("해당 룸이 존재하지 않습니다. 룸 ID: {}", roomId);
        }
    }
}



