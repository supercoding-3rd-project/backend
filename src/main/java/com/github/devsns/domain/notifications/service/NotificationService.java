package com.github.devsns.domain.notifications.service;

import com.github.devsns.domain.answers.entity.AnswerEntity;
import com.github.devsns.domain.comments.entity.AnswerCommentEntity;
import com.github.devsns.domain.comments.entity.QuestionCommentEntity;
import com.github.devsns.domain.question.entity.QuestionBoardEntity;
import com.github.devsns.domain.user.entitiy.UserEntity;
import org.springframework.web.socket.WebSocketSession;

public interface NotificationService {
    void sendRecentNotificationsToUser(Long userId, WebSocketSession session);

    void sendCommentNotification(UserEntity questionAuthor, QuestionCommentEntity comment);

    void sendCommentNotification(UserEntity answerer, AnswerCommentEntity comment);

    void sendLikeCommentNotification(UserEntity recipient, UserEntity liker, QuestionCommentEntity comment);

    void sendLikeCommentNotification(UserEntity recipient, UserEntity liker, AnswerCommentEntity comment);

    void sendLikeQuestionNotification(UserEntity recipient, UserEntity liker, QuestionBoardEntity question);
    void sendAnswerNotification(UserEntity recipient, UserEntity answerer, QuestionBoardEntity question);

    void sendMessageNotification(UserEntity recipient, UserEntity sender);

    void sendLikeAnswerNotification(UserEntity recipient, UserEntity liker, AnswerEntity answer);


}
