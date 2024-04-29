package com.github.devsns.domain.chat.repository;

import com.github.devsns.domain.chat.entity.ChatRoom;
import com.github.devsns.domain.chat.entity.QChatRoom;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 채팅방 정보에 대한 사용자 정의 쿼리를 구현하는 클래스.
 */
@Repository
public class ChatRoomRepositoryImpl implements ChatRoomRepositoryCustom{
    private final JPAQueryFactory queryFactory;
    @PersistenceContext
    private EntityManager entityManager;

    // EntityManager를 주입받아 JPAQueryFactory를 초기화하는 생성자
    public ChatRoomRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    /**
     * 사용자 ID를 기준으로 사용자가 참여한 모든 채팅방을 조회합니다.
     * @param userId 조회할 사용자의 ID
     * @return 사용자가 참여한 채팅방 목록
     */
    public List<ChatRoom> findCustomRoomsByParticipantId(Long userId) {
        QChatRoom chatRoom = QChatRoom.chatRoom;
        return queryFactory
                .selectFrom(chatRoom)
                .where(chatRoom.participant1.userId.eq(userId)
                        .or(chatRoom.participant2.userId.eq(userId)))
                .fetch();
    }



    /**
     * 주어진 두 사용자가 참여하는 채팅방을 찾습니다.
     * @param participant1 첫 번째 참여자의 이름
     * @param participant2 두 번째 참여자의 이름
     * @return 주어진 두 사용자가 참여하는 채팅방 (Optional)
     */
    public Optional<ChatRoom> findRoomByParticipants(String participant1, String participant2) {
        QChatRoom chatRoom = QChatRoom.chatRoom;
        ChatRoom room = queryFactory.selectFrom(chatRoom)
                .where(chatRoom.participant1.username.eq(participant1)
                        .and(chatRoom.participant2.username.eq(participant2))
                        .or(chatRoom.participant1.username.eq(participant2)
                                .and(chatRoom.participant2.username.eq(participant1))))
                .fetchOne();
        return Optional.ofNullable(room);
    }



    public List<ChatRoom> findRoomsByCurrentUserIdAndOtherUsername(Long currentUserId, String otherUsername) {
        QChatRoom chatRoom = QChatRoom.chatRoom;
        return queryFactory
                .selectFrom(chatRoom)
                .where(
                        chatRoom.participant1.userId.eq(currentUserId).and(chatRoom.participant2.username.eq(otherUsername))
                                .or(
                                        chatRoom.participant2.userId.eq(currentUserId).and(chatRoom.participant1.username.eq(otherUsername))
                                )
                )
                .fetch();
    }


}
