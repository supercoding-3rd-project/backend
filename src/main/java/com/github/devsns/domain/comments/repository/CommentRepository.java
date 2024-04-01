package com.github.devsns.domain.comments.repository;

import com.github.devsns.domain.comments.entity.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, Long> {

    List<CommentEntity> findbyUserId(String userId);

}
