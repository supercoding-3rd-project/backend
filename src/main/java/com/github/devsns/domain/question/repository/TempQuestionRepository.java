package com.github.devsns.domain.question.repository;

import com.github.devsns.domain.question.entity.TempQuestionEntity;
import com.github.devsns.domain.user.entitiy.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TempQuestionRepository extends JpaRepository<TempQuestionEntity, Long> {
    List<TempQuestionEntity> findByQuestioner(UserEntity user);
    void deleteByQuestionerAndId(UserEntity user, Long tempId);
}