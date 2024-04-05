package com.github.devsns.domain.comments.service;


import com.github.devsns.domain.comments.entity.AnswerCommentEntity;

import java.util.List;

public interface AnswerCommentService {

    void checkCommenter(String commentId, Long userId);

    void createAnswerComment(Long answerId, Long userId, String content);

    void updateAnswerComment(String commentId, String content);


    void deleteAnswerComment(String commentId);

//    List<AnswerCommentEntity> getAllComments(Long quesId);

}
