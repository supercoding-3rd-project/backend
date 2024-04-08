package com.github.devsns.domain.notifications.service;

import com.github.devsns.domain.answers.entity.AnswerEntity;
import com.github.devsns.domain.comments.entity.AnswerCommentEntity;
import com.github.devsns.domain.comments.entity.QuestionCommentEntity;
import com.github.devsns.domain.notifications.entity.Notification;
import com.github.devsns.domain.question.entity.QuestionBoardEntity;
import com.github.devsns.domain.user.entitiy.UserEntity;
import org.springframework.web.socket.WebSocketSession;

import java.util.List;

public interface NotificationService {

    void sendCommentNotification(UserEntity questionAuthor, QuestionCommentEntity comment);

    void sendCommentNotification(UserEntity answerer, AnswerCommentEntity comment);


    void sendAnswerNotification(UserEntity recipient, UserEntity answerer, QuestionBoardEntity question);


    void sendLikeAnswerNotification(UserEntity recipient, UserEntity liker, AnswerEntity answer);


    void sendMessageNotification(UserEntity recipient, UserEntity sender);


    void sendLikeQuestionNotification(UserEntity recipient, UserEntity liker, QuestionBoardEntity question);


    void deleteQuestionCommentNotification(QuestionCommentEntity comment);

    void deleteAnswerCommentNotification(AnswerCommentEntity comment);


    void deleteLikeQuestionNotification(QuestionBoardEntity question, UserEntity liker);


    void deleteLikeAnswerNotification(AnswerEntity answer, UserEntity liker);


    void deleteAnswerNotification(AnswerEntity answer, UserEntity answerer);


    void deleteMessageNotification(UserEntity recipient, UserEntity sender);




}
