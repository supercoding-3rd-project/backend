package com.github.devsns.domain.chat.entity;

import com.github.devsns.domain.user.entitiy.UserEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter@Setter
@Table(name = "chatRoom")
public class ChatRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String roomId;

    // User 엔터티와의 관계를 정의
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "participant1_id") // DB에서 사용할 컬럼명
    private UserEntity participant1;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "participant2_id") // DB에서 사용할 컬럼명
    private UserEntity participant2;


}
