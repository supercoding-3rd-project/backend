package com.github.devsns.domain.qnas.repository;

import com.github.devsns.domain.qnas.entity.QuestionBoardEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionBoardRepository extends JpaRepository<QuestionBoardEntity, Long> {

}
