package com.github.devsns.domain.follow.repository;

import com.github.devsns.domain.follow.entity.FollowEntity;
import com.github.devsns.domain.user.entitiy.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FollowRepository extends JpaRepository<FollowEntity, Long> {

    // 팔로우 구현
    @Query("select f from FollowEntity f where f.fromUser = :fromUser and f.toUser = :toUser")
    Optional<FollowEntity> findFollow(@Param("fromUser") UserEntity fromUser, @Param("toUser") UserEntity toUser);

    // 팔로우 취소 구현
    @Modifying
    @Query("delete from FollowEntity f where f.toUser = :toUser and f.fromUser = :fromUser")
    void deleteByToUserAndFromUser(@Param("toUser") UserEntity toUser, @Param("fromUser") UserEntity fromUser);

    List<FollowEntity> findByFromUser(UserEntity fromUser);

    List<FollowEntity> findByToUser(UserEntity toUser);

    Long countByFollowId(Long followId);
}
