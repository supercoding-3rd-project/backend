package com.github.devsns.domain.question.repository;

import com.github.devsns.domain.question.entity.QuestionLike;
import com.github.devsns.domain.user.entitiy.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<QuestionLike, Long> {
    Optional<QuestionLike> findByQuestionBoardIdAndUserId(Long questionBoardId, UserEntity user);
}