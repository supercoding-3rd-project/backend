package com.github.devsns.domain.notifications.service;

import com.github.devsns.domain.answers.entity.AnswerEntity;
import com.github.devsns.domain.comments.entity.AnswerCommentEntity;
import com.github.devsns.domain.comments.entity.QuestionCommentEntity;
import com.github.devsns.domain.notifications.constant.NotificationType;
import com.github.devsns.domain.notifications.entity.*;
import com.github.devsns.domain.notifications.repository.NotificationRepository;
import com.github.devsns.domain.question.entity.QuestionBoardEntity;
import com.github.devsns.domain.user.entitiy.UserEntity;
import jakarta.persistence.EntityManager;
import org.springframework.context.ApplicationEvent;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class NotificationServiceImpl implements NotificationService {
    private final NotificationRepository notificationRepository;
    private final EntityManager entityManager;

    public NotificationServiceImpl(NotificationRepository notificationRepository, EntityManager entityManager) {
        this.entityManager = entityManager;
        this.notificationRepository = notificationRepository;
    }

    @Transactional
    public void sendCommentNotification(UserEntity questionAuthor, QuestionCommentEntity comment) {
        Notification notification = new Notification();
        notification.setRecipient(questionAuthor);

        UserEntity commenter = comment.getCommenter();
        QuestionBoardEntity question = comment.getQuestion();

        QuestionCommentNotification questionCommentNotification = new QuestionCommentNotification();
        questionCommentNotification.setCommenter(commenter);
        questionCommentNotification.setQuestion(question);

        questionCommentNotification.setComment(comment);

        // QuestionCommentNotification 엔티티를 저장
        entityManager.persist(questionCommentNotification);

        notification.setQuestionCommentNotification(questionCommentNotification);
        notification.setType(NotificationType.QUESTION_COMMENT);
        notificationRepository.save(notification);
    }

    @Transactional
    public void sendCommentNotification(UserEntity answerAuthor, AnswerCommentEntity comment) {
        Notification notification = new Notification();
        notification.setRecipient(answerAuthor);

        UserEntity commenter = comment.getCommenter();
        AnswerEntity answer = comment.getAnswer();

        AnswerCommentNotification answerCommentNotification = new AnswerCommentNotification();
        answerCommentNotification.setCommenter(commenter);
        answerCommentNotification.setAnswer(answer);

        answerCommentNotification.setComment(comment);

        // AnswerCommentNotification 엔티티를 저장
        entityManager.persist(answerCommentNotification);

        notification.setAnswerCommentNotification(answerCommentNotification);
        notification.setType(NotificationType.ANSWER_COMMENT);
        notificationRepository.save(notification);
    }

    @Transactional
    public void sendLikeQuestionNotification(UserEntity recipient, UserEntity liker, QuestionBoardEntity question) {
        Notification notification = new Notification();
        notification.setRecipient(recipient);

        LikeQuestionNotification likeQuestionNotification = new LikeQuestionNotification();
        likeQuestionNotification.setLiker(liker);
        likeQuestionNotification.setQuestion(question);

        // LikeQuestionNotification 엔티티를 저장
        entityManager.persist(likeQuestionNotification);

        notification.setLikeQuestionNotification(likeQuestionNotification);
        notification.setType(NotificationType.QUESTION_LIKE);
        notificationRepository.save(notification);
    }

    @Transactional
    public void sendLikeAnswerNotification(UserEntity recipient, UserEntity liker, AnswerEntity answer) {
        Notification notification = new Notification();
        notification.setRecipient(recipient);

        LikeAnswerNotification likeAnswerNotification = new LikeAnswerNotification();
        likeAnswerNotification.setLiker(liker);
        likeAnswerNotification.setAnswer(answer);

        // LikeAnswerNotification 엔티티를 저장
        entityManager.persist(likeAnswerNotification);

        notification.setLikeAnswerNotification(likeAnswerNotification);
        notification.setType(NotificationType.ANSWER_LIKE);
        notificationRepository.save(notification);
    }

    @Transactional
    public void sendAnswerNotification(UserEntity recipient, UserEntity answerer, QuestionBoardEntity question) {
        Notification notification = new Notification();
        notification.setRecipient(recipient);

        AnswerNotification answerNotification = new AnswerNotification();
        answerNotification.setAnswerer(answerer);
        answerNotification.setQuestion(question); //1대1

        // AnswerNotification 엔티티를 저장
        entityManager.persist(answerNotification);

        notification.setAnswerNotification(answerNotification);
        notification.setType(NotificationType.ANSWER);
        notificationRepository.save(notification);
    }

    @Transactional
    public void sendFollowNotification(UserEntity recipient, UserEntity follower) {
        Notification notification = new Notification();
        notification.setRecipient(recipient);

        FollowNotification followNotification = new FollowNotification();
        followNotification.setFollower(follower);

        // FollowNotification 엔티티를 저장
        entityManager.persist(followNotification);

        notification.setFollowNotification(followNotification);
        notification.setType(NotificationType.FOLLOW);
        notificationRepository.save(notification);
    }


    @Transactional
    public void sendMessageNotification(UserEntity recipient, UserEntity sender) {
        Notification notification = new Notification();
        notification.setRecipient(recipient);

        MessageNotification messageNotification = new MessageNotification();
        messageNotification.setSender(sender);

        // MessageNotification 엔티티를 저장
        entityManager.persist(messageNotification);

        notification.setMessageNotification(messageNotification);
        notification.setType(NotificationType.MESSAGE);
        notificationRepository.save(notification);
    }

    @Transactional
    public void deleteQuestionCommentNotification(QuestionCommentEntity comment) {
        Notification notification = notificationRepository.findByQuestionCommentNotification_Comment(comment)
                .orElse(null);
        if (notification != null) {
            notificationRepository.delete(notification);
        }
    }

    @Transactional
    public void deleteAnswerCommentNotification(AnswerCommentEntity comment) {
        Notification notification = notificationRepository.findByAnswerCommentNotification_Comment(comment)
                .orElse(null);
        if (notification != null) {
            notificationRepository.delete(notification);
        }
    }

    @Transactional
    public void deleteLikeQuestionNotification(QuestionBoardEntity question, UserEntity liker) {
        Notification notification = notificationRepository.findByLikeQuestionNotification_Question_AndLikeQuestionNotification_Liker(question, liker)
                .orElse(null);
        if (notification != null) {
            notificationRepository.delete(notification);
        }
    }

    @Transactional
    public void deleteLikeAnswerNotification(AnswerEntity answer, UserEntity liker) {
        Notification notification = notificationRepository.findByLikeAnswerNotification_Answer_AndLikeAnswerNotification_Liker(answer, liker)
                .orElse(null);
        if (notification != null) {
            notificationRepository.delete(notification);
        }
    }

    @Transactional
    public void deleteAnswerNotification(AnswerEntity answer, UserEntity answerer) {
        notificationRepository.deleteByAnswerNotification_Question_AndAnswerNotification_Answerer(answer.getQuestion(), answerer);
    }


    @Transactional
    public void deleteMessageNotification(UserEntity recipient, UserEntity sender) {
        Notification notification = notificationRepository.findByMessageNotification_Sender_AndRecipient(sender, recipient)
                .orElse(null);
        if (notification != null) {
            notificationRepository.delete(notification);
        }
    }
}
