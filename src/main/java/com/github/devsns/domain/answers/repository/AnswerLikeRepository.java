package com.github.devsns.domain.answers.repository;

import com.github.devsns.domain.answers.entity.AnswerLike;
import com.github.devsns.domain.user.entitiy.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AnswerLikeRepository extends JpaRepository<AnswerLike, Long> {

    Optional<AnswerLike> findByAnswerIdAndUserId(Long answerId, UserEntity user);
}


