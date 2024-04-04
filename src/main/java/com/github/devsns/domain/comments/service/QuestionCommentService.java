package com.github.devsns.domain.comments.service;


import com.github.devsns.domain.comments.entity.QuestionCommentEntity;

import java.util.List;

public interface QuestionCommentService {

    void checkCommenter(String commentId, Long userId);
    void createQuestionComment(Long quesId, Long userId, String content);

    void updateQuestionComment(String commentId, String content);

    void deleteQuestionComment(String commentId);

//    List<QuestionCommentEntity> getAllComments(Long quesId);
}
