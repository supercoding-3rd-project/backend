package com.github.devsns.domain.chat.service;


import com.github.devsns.domain.chat.dto.RoomDto;
import com.github.devsns.domain.chat.dto.RoomResponse;
import com.github.devsns.domain.chat.entity.ChatMessage;
import com.github.devsns.domain.chat.entity.ChatRoom;
import com.github.devsns.domain.chat.repository.ChatRoomRepository;
import com.github.devsns.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class RoomService {
    private final Map<String, RoomDto> roomMap = new HashMap<>();
    private final UserRepository userRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatService chatService;

    // 특정 사용자와의 채팅방을 조회 또는 생성하는 메서드
    public RoomResponse getOrCreateRoom(String senderId, String recipientId) {
        log.info("사용자 '{}'와 '{}' 사이의 채팅방을 조회하거나 생성합니다.", senderId, recipientId);
        // 특정 사용자와의 채팅방이 이미 존재하는지 확인
        Optional<ChatRoom> existingRoom = chatRoomRepository.findRoomByParticipants(senderId, recipientId);

        if (existingRoom.isPresent()) {
            log.info("채팅방이 이미 존재합니다. 채팅방 ID: {}", existingRoom.get().getId());
            return RoomResponse.builder()
                    .roomId(existingRoom.get().getId())
                    .recipientUsername(recipientId)  // 여기서 recipientId는 실제로 username이어야 합니다.
                    .senderUsername(senderId)  // senderId도 마찬가지로 username이어야 합니다.
                    .lastMessage(null)
                    .lastMessageTime(null)
                    .isNew(false)
                    .build();
        } else {
            ChatRoom newRoom = new ChatRoom(senderId, recipientId);
            chatRoomRepository.save(newRoom);
            log.info("새 채팅방이 생성되었습니다. 채팅방 ID: {}", newRoom.getId());
            return RoomResponse.builder()
                    .roomId(newRoom.getId())
                    .recipientUsername(recipientId)
                    .senderUsername(senderId)
                    .lastMessage(null)
                    .lastMessageTime(null)
                    .isNew(true)
                    .build();
        }
    }

      // 사용자가 포함된 모든 채팅방의 목록을 조회하는 메서드
    public List<RoomResponse> getUserRooms(String userId) {
        log.info("사용자 '{}'가 참여한 모든 채팅방 목록을 조회합니다.", userId);


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

            roomResponses.add(RoomResponse.builder()
                    .roomId(roomId)
                    .recipientUsername(recipientId)
                    .lastMessage(lastMessage != null ? lastMessage.getContent() : null)
                    .lastMessageTime(lastMessage != null ? lastMessage.getTimestamp() : null)
                    .isNew(false)
                    .build());
        }

        return roomResponses;
    }

    //닉네임으로 채팅방을 찾는 메서드
    /**
     * 특정 닉네임을 포함하는 사용자를 검색하고 해당 사용자와 관련된 채팅방을 반환합니다.
     * 검색된 사용자가 참여한 채팅방 중에서 현재 사용자와의 대화방만 필터링하여 반환합니다.
     *
     */
    public List<RoomResponse> searchChatRoomsByNickname(String currentUserId, String keywordUsername) {
        try {
            List<ChatRoom> rooms = chatRoomRepository.findRoomsByParticipant1_UsernameContainingOrParticipant2_UsernameContaining(keywordUsername, keywordUsername);
            return rooms.stream()
                    .map(room -> new RoomResponse(
                            room.getId(),
                            room.getParticipant1().getUserId().equals(currentUserId) ? room.getParticipant2().getUsername() : room.getParticipant1().getUsername(),
                            null, // Assuming lastMessage content
                            null, // Assuming lastMessage timestamp
                            room.isNew()))
                    .collect(Collectors.toList());
        }   catch (Exception e) {
            // 예외 처리
            log.error("Error occurred while searching chat rooms by nickname: {}", e.getMessage());
            return Collections.emptyList(); // 예외 발생 시 빈 리스트 반환 또는 다른 처리 방법 선택
        }

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


//    // 룸에 세션 추가(웹소켓용)
//    public void addSessionToRoom(String roomId, WebSocketSession session) {
//        RoomDto roomDto = roomMap.get(roomId);
//        if (roomDto != null) {
//            roomDto.addSession(session);
//            log.info("룸에 세션을 추가했습니다. 세션 ID: {}, 룸 ID: {}, 총 세션 수: {}", session.getId(), roomId, roomDto.getSessionCount());
//        } else {
//            log.error("해당 룸이 존재하지 않습니다. 룸 ID: {}", roomId);
//        }
//    }
//
//    // 룸에서 세션 제거(웹소켓용)
//    public void removeSessionFromRoom(String roomId, WebSocketSession session) {
//        RoomDto roomDto = roomMap.get(roomId);
//        if (roomDto != null) {
//            roomDto.removeSession(session);
//            log.info("룸에서 세션을 제거했습니다. 세션 ID: {}, 룸 ID: {}, 총 세션 수: {}", session.getId(), roomId, roomDto.getSessionCount());
//            if (roomDto.isEmpty()) {
//                roomMap.remove(roomId);
//                log.info("룸이 비어있어 삭제했습니다. 룸 ID: {}", roomId);
//            }
//        } else {
//            log.error("해당 룸이 존재하지 않습니다. 룸 ID: {}", roomId);
//        }
//    }
}



