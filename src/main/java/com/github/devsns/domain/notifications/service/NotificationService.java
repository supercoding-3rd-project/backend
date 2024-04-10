package com.github.devsns.domain.notifications.service;

import com.github.devsns.domain.answers.entity.AnswerEntity;
import com.github.devsns.domain.comments.entity.AnswerCommentEntity;
import com.github.devsns.domain.comments.entity.QuestionCommentEntity;
import com.github.devsns.domain.notifications.entity.LikeAnswerNotification;
import com.github.devsns.domain.notifications.entity.LikeQuestionNotification;
import com.github.devsns.domain.notifications.entity.Notification;
import com.github.devsns.domain.question.entity.QuestionBoardEntity;
import com.github.devsns.domain.user.entitiy.UserEntity;
import org.springframework.web.socket.WebSocketSession;

import java.util.List;

public interface NotificationService {

    void sendQuestionCommentNotification(UserEntity recipient, QuestionCommentEntity comment);

    void sendAnswerCommentNotification(UserEntity recipient, AnswerCommentEntity comment);


    void sendAnswerNotification(UserEntity recipient, UserEntity answerer, QuestionBoardEntity question);


    void sendLikeAnswerNotification(UserEntity recipient, UserEntity liker, AnswerEntity answer);


    void sendLikeQuestionNotification(UserEntity recipient, UserEntity liker, QuestionBoardEntity question);


    void deleteQuestionCommentNotification(Long commentId);

    void deleteAnswerCommentNotification(Long commentId);


    void deleteByLikeQuestionNotification(Long likerId, Long questionId);


    void deleteByLikeAnswerNotification(Long likerId, Long answerId);


    void deleteAnswerNotification(Long answererId, Long questionId);

//    void sendMessageNotification(UserEntity recipient, UserEntity sender);
//    void deleteMessageNotification(Long senderId, Long recipientId);

}
