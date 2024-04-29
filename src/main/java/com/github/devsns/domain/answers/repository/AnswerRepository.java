package com.github.devsns.domain.answers.repository;

import com.github.devsns.domain.answers.entity.AnswerEntity;
import com.github.devsns.domain.user.entitiy.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnswerRepository extends JpaRepository<AnswerEntity, Long> {
    List<AnswerEntity> findAllByAnswererOrderByCreatedAtDesc(UserEntity userEntity);
}
