package com.github.devsns.domain.comments.service;


public interface CommentService {
    void createComment(Long postId, Long userId, String content);

}
