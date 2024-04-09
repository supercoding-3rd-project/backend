package com.github.devsns.domain.question.repository;

import com.github.devsns.domain.question.entity.LikeEntity;
import com.github.devsns.domain.question.entity.QuestionBoardEntity;
import com.github.devsns.domain.user.entitiy.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<LikeEntity, Long> {
    Optional<LikeEntity> findByUserAndQuestionBoard(UserEntity user, QuestionBoardEntity questionBoard);

    Long countByQuestionBoard(QuestionBoardEntity questionBoard);
}
