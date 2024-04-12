package com.github.devsns.domain.answers.repository;

import com.github.devsns.domain.answers.entity.AnswerLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnswerLikeRepository extends JpaRepository<AnswerLike, Long> {


}
