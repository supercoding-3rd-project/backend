package com.github.devsns.domain.comments.repository;

import com.github.devsns.domain.comments.entity.AnswerCommentEntity;
import com.github.devsns.domain.comments.entity.QuestionCommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface AnswerCommentRepository extends JpaRepository<AnswerCommentEntity, Long> {


}
