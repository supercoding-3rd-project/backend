package com.github.devsns.domain.chat.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RoomDto {
    private String id;
//    private Set<WebSocketSession> sessions = ConcurrentHashMap.newKeySet();

    public RoomDto(String id) {this.id = id;
    }

//    public void addSession(WebSocketSession session) {
//        sessions.add(session);
//    }

//    public void removeSession(WebSocketSession session) {
//        sessions.remove(session);
//    }

//    public boolean isEmpty() {
//        return sessions.isEmpty();
//    }
//
//    public int getSessionCount() {
//        return sessions.size();
//    }
}

