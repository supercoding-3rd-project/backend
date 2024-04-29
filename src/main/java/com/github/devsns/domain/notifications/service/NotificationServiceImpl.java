package com.github.devsns.domain.notifications.service;

import com.github.devsns.domain.answers.entity.AnswerEntity;
import com.github.devsns.domain.comments.entity.AnswerCommentEntity;
import com.github.devsns.domain.notifications.constant.NotificationType;
import com.github.devsns.domain.notifications.entity.*;
import com.github.devsns.domain.notifications.repository.*;
import com.github.devsns.domain.question.entity.QuestionBoardEntity;
import com.github.devsns.domain.user.entitiy.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private final NotificationRepository notificationRepository;
    private final AnswerCommentNotificationRepository answerCommentNotificationRepository;
    private final AnswerNotificationRepository answerNotificationRepository;
    private final LikeAnswerNotificationRepository likeAnswerNotificationRepository;
    private final LikeQuestionNotificationRepository likeQuestionNotificationRepository;
    private final FollowNotificationRepository followNotificationRepository;


    @Transactional
    public void sendAnswerCommentNotification(UserEntity recipient, AnswerCommentEntity comment) {
        // Notification 엔티티 생성

        AnswerCommentNotification answerCommentNotification = new AnswerCommentNotification();
        answerCommentNotification.setRecipientId(recipient.getUserId());
        answerCommentNotification.setRecipient(recipient.getUsername());
        answerCommentNotification.setType(NotificationType.ANSWER_COMMENT);
        answerCommentNotification.setCreatedAt(LocalDateTime.now());
        answerCommentNotification.setRead(false);
        answerCommentNotification.setCommenterId(comment.getCommenter().getUserId());
        answerCommentNotification.setCommenter(comment.getCommenter().getUsername());
        answerCommentNotification.setAnswerId(comment.getAnswer().getId());
        answerCommentNotification.setProfileImage(comment.getCommenter().getImageUrl());
        answerCommentNotification.setCommentId(comment.getId());

        // Notification 엔티티 저장
        notificationRepository.save(answerCommentNotification);
    }

    @Transactional
    public void sendLikeQuestionNotification(UserEntity recipient, UserEntity liker, QuestionBoardEntity question) {

        // LikeQuestionNotification 엔티티 생성
        LikeQuestionNotification likeQuestionNotification = new LikeQuestionNotification();
        likeQuestionNotification.setRecipientId(recipient.getUserId());
        likeQuestionNotification.setRecipient(recipient.getUsername());
        likeQuestionNotification.setType(NotificationType.QUESTION_LIKE);
        likeQuestionNotification.setCreatedAt(LocalDateTime.now());
        likeQuestionNotification.setRead(false);
        likeQuestionNotification.setLikerId(liker.getUserId());
        likeQuestionNotification.setLiker(liker.getUsername());
        likeQuestionNotification.setProfileImage(liker.getImageUrl());
        likeQuestionNotification.setQuestionId(question.getId());

        // Notification 엔티티 저장
        notificationRepository.save(likeQuestionNotification);
    }

    @Transactional
    public void sendDislikeQuestionNotification(UserEntity recipient, UserEntity liker, QuestionBoardEntity question) {

        // LikeQuestionNotification 엔티티 생성
        LikeQuestionNotification likeQuestionNotification = new LikeQuestionNotification();
        likeQuestionNotification.setRecipientId(recipient.getUserId());
        likeQuestionNotification.setRecipient(recipient.getUsername());
        likeQuestionNotification.setType(NotificationType.QUESTION_DISLIKE);
        likeQuestionNotification.setCreatedAt(LocalDateTime.now());
        likeQuestionNotification.setRead(false);
        likeQuestionNotification.setLikerId(liker.getUserId());
        likeQuestionNotification.setLiker(liker.getUsername());
        likeQuestionNotification.setProfileImage(liker.getImageUrl());
        likeQuestionNotification.setQuestionId(question.getId());
    }

    @Transactional
    public void sendLikeAnswerNotification(UserEntity recipient, UserEntity liker, AnswerEntity answer) {

        // LikeAnswerNotification 엔티티 생성
        LikeAnswerNotification likeAnswerNotification = new LikeAnswerNotification();
        likeAnswerNotification.setRecipientId(recipient.getUserId());
        likeAnswerNotification.setRecipient(recipient.getUsername());
        likeAnswerNotification.setType(NotificationType.ANSWER_LIKE);
        likeAnswerNotification.setCreatedAt(LocalDateTime.now());
        likeAnswerNotification.setRead(false);
        likeAnswerNotification.setLikerId(liker.getUserId());
        likeAnswerNotification.setLiker(liker.getUsername());
        likeAnswerNotification.setProfileImage(liker.getImageUrl());
        likeAnswerNotification.setAnswerId(answer.getId());

        // Notification 엔티티 저장
        notificationRepository.save(likeAnswerNotification);
    }

//    @Transactional
//    public void sendDislikeAnswerNotification(UserEntity recipient, UserEntity liker, AnswerEntity answer) {
//
//        // LikeAnswerNotification 엔티티 생성
//        LikeAnswerNotification likeAnswerNotification = new LikeAnswerNotification();
//        likeAnswerNotification.setRecipientId(recipient.getUserId());
//        likeAnswerNotification.setType(NotificationType.ANSWER_DISLIKE);
//        likeAnswerNotification.setCreatedAt(LocalDateTime.now());
//        likeAnswerNotification.setRead(false);
//        likeAnswerNotification.setLikerId(liker.getUserId());
//        likeAnswerNotification.setLiker(liker.getUsername());
//        likeAnswerNotification.setAnswerId(answer.getId());
//
//        // Notification 엔티티 저장
//        notificationRepository.save(likeAnswerNotification);
//    }


    @Transactional
    public void sendAnswerNotification(UserEntity recipient, UserEntity answerer, QuestionBoardEntity question) {

        // AnswerNotification 엔티티 생성
        AnswerNotification answerNotification = new AnswerNotification();
        answerNotification.setRecipientId(recipient.getUserId());
        answerNotification.setRecipient(recipient.getUsername());
        answerNotification.setType(NotificationType.ANSWER);
        answerNotification.setCreatedAt(LocalDateTime.now());
        answerNotification.setRead(false);
        answerNotification.setAnswererId(answerer.getUserId());
        answerNotification.setAnswerer(answerer.getUsername());
        answerNotification.setProfileImage(answerer.getImageUrl());
        answerNotification.setQuestionId(question.getId()); //1대1

        // Notification 엔티티 저장
        notificationRepository.save(answerNotification);
    }

    @Transactional
    public void sendFollowNotification(UserEntity recipient, UserEntity follower) {

        // FollowNotification 엔티티 생성
        FollowNotification followNotification = new FollowNotification();
        followNotification.setRecipientId(recipient.getUserId());
        followNotification.setRecipient(recipient.getUsername());
        followNotification.setType(NotificationType.FOLLOW);
        followNotification.setCreatedAt(LocalDateTime.now());
        followNotification.setRead(false);
        followNotification.setFollowerId(follower.getUserId());
        followNotification.setFollower(follower.getUsername());
        followNotification.setProfileImage(follower.getImageUrl());

        // Notification 엔티티 저장
        notificationRepository.save(followNotification);
    }


    @Transactional
    public void deleteAnswerCommentNotification(Long commentId) {
        answerCommentNotificationRepository.deleteByCommentId(commentId);
    }

    @Transactional
    public void deleteAnswerNotification(Long answererId, Long questionId) {
        answerNotificationRepository.deleteByAnswererIdAndQuestionId(answererId, questionId);
    }

    @Transactional
    public void deleteFollowingNotification(Long recipientId, Long followerId) {
        followNotificationRepository.deleteByRecipientIdAndFollowerId(
                recipientId, followerId);
    }

    @Transactional
    public void deleteLikeAnswerNotification(Long recipientId, Long likerId) {
        likeAnswerNotificationRepository.deleteByRecipientIdAndLikerId(recipientId, likerId);
    }

    @Transactional
    public void deleteLikeQuestionNotification(Long recipientId, Long likerId) {
        likeQuestionNotificationRepository.deleteByRecipientIdAndLikerId(recipientId, likerId);
    }

}
