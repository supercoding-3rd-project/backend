package com.github.devsns.domain.question.repository;

import com.github.devsns.domain.question.entity.QuestionBoardEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionBoardRepository extends JpaRepository<QuestionBoardEntity, Long> {

}
