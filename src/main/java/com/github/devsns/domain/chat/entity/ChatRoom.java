package com.github.devsns.domain.chat.entity;

import com.github.devsns.domain.user.entitiy.UserEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter@Setter
@Table(name = "chat_rooms")
public class ChatRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
   private String id;

    @Column(name = "room_id", updatable = false, nullable = false, unique = true)
    private String roomId;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "participant1_id") // DB에서 사용할 컬럼명
    private UserEntity participant1;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "participant2_id") // DB에서 사용할 컬럼명
    private UserEntity participant2;

    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ChatMessage> messages = new HashSet<>();

    @Column(nullable = false)
    private LocalDateTime creationTimestamp = LocalDateTime.now();

    //마지막메세지 호출
    public ChatMessage getLastMessage() {
        return messages.stream()
                .max(Comparator.comparing(ChatMessage::getTimestamp))
                .orElse(null);
    }

    //새로 생성되는 방 확인 여부
    public boolean isNew() {
        // 방이 생성된 후 24시간 이내인지 확인
        return creationTimestamp.isAfter(LocalDateTime.now().minusDays(1));
    }
}
