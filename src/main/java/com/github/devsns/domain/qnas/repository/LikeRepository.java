package com.github.devsns.domain.qnas.repository;

import com.github.devsns.domain.qnas.entity.LikeEntity;
import com.github.devsns.domain.qnas.entity.QuestionBoardEntity;
import com.github.devsns.domain.user.userEntity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<LikeEntity, Long> {
    Optional<LikeEntity> findByUserAndQuestionBoard(UserEntity user, QuestionBoardEntity post);
}
