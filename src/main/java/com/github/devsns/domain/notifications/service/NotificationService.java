package com.github.devsns.domain.notifications.service;

import com.github.devsns.domain.answers.entity.AnswerEntity;
import com.github.devsns.domain.comments.entity.AnswerCommentEntity;
import com.github.devsns.domain.question.entity.QuestionBoardEntity;
import com.github.devsns.domain.user.entitiy.UserEntity;

public interface NotificationService {

    void sendAnswerCommentNotification(UserEntity recipient, AnswerCommentEntity comment);


    void sendAnswerNotification(UserEntity recipient, UserEntity answerer, QuestionBoardEntity question);


    void sendLikeAnswerNotification(UserEntity recipient, UserEntity liker, AnswerEntity answer);


    void sendLikeQuestionNotification(UserEntity recipient, UserEntity liker, QuestionBoardEntity question);


    void deleteAnswerCommentNotification(Long commentId);


    void deleteAnswerNotification(Long answererId, Long questionId);


//    void sendMessageNotification(UserEntity recipient, UserEntity sender);
//    void deleteMessageNotification(Long senderId, Long recipientId);

}
