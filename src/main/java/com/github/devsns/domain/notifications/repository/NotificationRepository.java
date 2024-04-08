package com.github.devsns.domain.notifications.repository;

import com.github.devsns.domain.answers.entity.AnswerEntity;
import com.github.devsns.domain.comments.entity.AnswerCommentEntity;
import com.github.devsns.domain.comments.entity.QuestionCommentEntity;
import com.github.devsns.domain.notifications.entity.Notification;
import com.github.devsns.domain.question.entity.QuestionBoardEntity;
import com.github.devsns.domain.user.entitiy.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findAllByRecipientUserIdOrderByCreatedAtDesc(Long userId);

    List<Notification> deleteByCreatedAtBefore(LocalDateTime dateTime);

    Optional<Notification> findByQuestionCommentNotification_Comment(QuestionCommentEntity comment);

    Optional<Notification> findByAnswerCommentNotification_Comment(AnswerCommentEntity comment);

    Optional<Notification> findByLikeQuestionNotification_Question_AndLikeQuestionNotification_Liker(QuestionBoardEntity question, UserEntity liker);

    Optional<Notification> findByLikeAnswerNotification_Answer_AndLikeAnswerNotification_Liker(AnswerEntity answer, UserEntity liker);

    Optional<Notification> deleteByAnswerNotification_Question_AndAnswerNotification_Answerer(QuestionBoardEntity question, UserEntity answerer);

    Optional<Notification> findByMessageNotification_Sender_AndRecipient(UserEntity sender, UserEntity recipient);
}