package com.github.devsns.domain.question.repository;

import com.github.devsns.domain.question.entity.QuestionBoardEntity;
import com.github.devsns.domain.user.entitiy.UserEntity;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionBoardRepository extends JpaRepository<QuestionBoardEntity, Long>{

//    List<QuestionBoardEntity> findQuestionBoardEntitiesByTitleContaining(String keyword);


    List<QuestionBoardEntity> findAll(Sort sort);

    List<QuestionBoardEntity> findByTitleContainingOrContentContaining(String titleKeyword, String contentKeyword);

    List<QuestionBoardEntity> findAllByQuestionerOrderByCreatedAtDesc(UserEntity user);
}
