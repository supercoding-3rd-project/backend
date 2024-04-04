package com.github.devsns.domain.notifications.service;

import com.github.devsns.domain.answers.entity.AnswerEntity;
import com.github.devsns.domain.comments.entity.AnswerCommentEntity;
import com.github.devsns.domain.comments.entity.QuestionCommentEntity;
import com.github.devsns.domain.notifications.constant.NotificationType;
import com.github.devsns.domain.notifications.entity.*;
import com.github.devsns.domain.notifications.repository.NotificationRepository;
import com.github.devsns.domain.question.entity.QuestionBoardEntity;
import com.github.devsns.domain.user.entitiy.UserEntity;
import org.springframework.context.ApplicationEvent;
import org.springframework.stereotype.Service;

@Service
public class NotificationServiceImpl implements NotificationService {
    private final NotificationRepository notificationRepository;


    public NotificationServiceImpl(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public void sendCommentNotification(UserEntity questionAuthor, QuestionCommentEntity comment) {
        Notification notification = new Notification();
        notification.setRecipient(questionAuthor);

        UserEntity commenter = comment.getCommenter();
        QuestionBoardEntity question = comment.getQuestion();

        QuestionCommentNotification questionCommentNotification = new QuestionCommentNotification();
        questionCommentNotification.setCommenter(commenter);
        questionCommentNotification.setQuestion(question);

        questionCommentNotification.setComment(comment);

        notification.setQuestionCommentNotification(questionCommentNotification);
        notification.setType(NotificationType.QUESTION_COMMENT);
        notificationRepository.save(notification);
    }

    @Override
    public void sendCommentNotification(UserEntity answerAuthor,AnswerCommentEntity comment) {
        Notification notification = new Notification();
        notification.setRecipient(answerAuthor);

        UserEntity commenter = comment.getCommenter();
        AnswerEntity answer = comment.getAnswer();

        AnswerCommentNotification answerCommentNotification = new AnswerCommentNotification();
        answerCommentNotification.setCommenter(commenter);
        answerCommentNotification.setAnswer(answer);

        answerCommentNotification.setComment(comment);

        notification.setAnswerCommentNotification(answerCommentNotification);
        notification.setType(NotificationType.ANSWER_COMMENT);
        notificationRepository.save(notification);
    }


    public void sendLikeQuestionNotification(UserEntity recipient, UserEntity liker, QuestionBoardEntity question) {
        Notification notification = new Notification();
        notification.setRecipient(recipient);

        LikeQuestionNotification likeQuestionNotification = new LikeQuestionNotification();
        likeQuestionNotification.setLiker(liker);
        likeQuestionNotification.setQuestion(question);

        notification.setLikeQuestionNotification(likeQuestionNotification);
        notification.setType(NotificationType.QUESTION_LIKE);
        notificationRepository.save(notification);
    }


    public void sendLikeAnswerNotification(UserEntity recipient, UserEntity liker, AnswerEntity answer) {
        Notification notification = new Notification();
        notification.setRecipient(recipient);

        LikeAnswerNotification likeAnswerNotification = new LikeAnswerNotification();
        likeAnswerNotification.setLiker(liker);
        likeAnswerNotification.setAnswer(answer);

        notification.setLikeAnswerNotification(likeAnswerNotification);
        notification.setType(NotificationType.ANSWER_LIKE);
        notificationRepository.save(notification);
    }

    public void sendAnswerNotification(UserEntity recipient, UserEntity answerer, QuestionBoardEntity question) {
        Notification notification = new Notification();
        notification.setRecipient(recipient);

        AnswerNotification answerNotification = new AnswerNotification();
        answerNotification.setAnswerer(answerer);
        answerNotification.setQuestion(question);

        notification.setAnswerNotification(answerNotification);
        notification.setType(NotificationType.ANSWER);
        notificationRepository.save(notification);
    }



    public void sendMessageNotification(UserEntity recipient, UserEntity sender) {
        Notification notification = new Notification();
        notification.setRecipient(recipient);

        MessageNotification messageNotification = new MessageNotification();
        messageNotification.setSender(sender);

        notification.setType(NotificationType.MESSAGE);
        notificationRepository.save(notification);
    }
}
