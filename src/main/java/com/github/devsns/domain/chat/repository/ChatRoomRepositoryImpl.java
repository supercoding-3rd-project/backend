package com.github.devsns.domain.chat.repository;

import com.github.devsns.domain.chat.entity.ChatRoom;
import com.github.devsns.domain.chat.entity.QChatRoom;
import com.github.devsns.domain.user.entitiy.UserEntity;
import com.querydsl.core.types.Expression;
import com.querydsl.jpa.impl.JPAQueryFactory; // JPAQueryFactory 임포트
import org.springframework.beans.factory.annotation.Autowired; // Spring의 Autowired 사용
import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager; // EntityManager 임포트
import jakarta.persistence.PersistenceContext;
import java.util.List;
@Repository
public class ChatRoomRepositoryImpl implements ChatRoomRepositoryCustom{
    private final JPAQueryFactory queryFactory;
    @PersistenceContext
    private EntityManager entityManager;

    // EntityManager를 주입받아 JPAQueryFactory를 생성
    public ChatRoomRepositoryImpl() {
        this.queryFactory = new JPAQueryFactory(() -> entityManager);
    }



    @Override
    public List<ChatRoom> findChatRoomsByParticipant(UserEntity participant) {
        QChatRoom chatRoom = QChatRoom.chatRoom; // QChatRoom 클래스의 정확한 임포트 확인 필요
        return queryFactory.selectFrom(chatRoom)
                .where(chatRoom.participant1.eq((Expression<? super UserEntity>) participant)
                        .or(chatRoom.participant2.eq((Expression<? super UserEntity>) participant)))
                .fetch();
    }


    @Override
    public List<ChatRoom> findRoomsByRecipientNameContaining(String username, Long participantId) {
        QChatRoom chatRoom = QChatRoom.chatRoom;
        return queryFactory.selectFrom(chatRoom)
                .where(chatRoom.participant1.userId.eq(participantId)
                        .or(chatRoom.participant2.userId.eq(participantId)))
                .fetch();
    }


}
