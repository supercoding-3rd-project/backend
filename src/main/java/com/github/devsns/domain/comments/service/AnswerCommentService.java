package com.github.devsns.domain.comments.service;


public interface AnswerCommentService {

    void checkCommenter(String commentId, Long userId);

    void createAnswerComment(Long answerId, Long userId, String content);

    void updateAnswerComment(String commentId, String content);


    void deleteAnswerComment(String commentId);


}
