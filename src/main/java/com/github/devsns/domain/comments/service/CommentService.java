package com.github.devsns.domain.comments.service;


public interface CommentService {

    void checkCommenter(String commentId, Long userId);
    void createQuestionComment(Long quesId, Long userId, String content, Long parentCommentId);

    void createAnswerComment(Long answerId, Long userId, String content, Long parentCommentId);

    void updateComment(String commentId, String content);

    void likeComment(String commentId, Long userId);
    void unlikeComment(String commentId, Long userId);

    void deleteComment(String commentId);

}
