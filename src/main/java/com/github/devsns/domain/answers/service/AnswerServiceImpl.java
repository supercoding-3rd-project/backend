package com.github.devsns.domain.answers.service;

import com.github.devsns.domain.answers.entity.AnswerEntity;
import com.github.devsns.domain.answers.entity.AnswerLike;
import com.github.devsns.domain.answers.repository.AnswerLikeRepository;
import com.github.devsns.domain.answers.repository.AnswerRepository;
import com.github.devsns.domain.comments.entity.AnswerCommentEntity;
import com.github.devsns.domain.comments.repository.AnswerCommentRepository;
import com.github.devsns.domain.notifications.constant.NotificationType;
import com.github.devsns.domain.notifications.entity.Notification;
import com.github.devsns.domain.notifications.repository.NotificationRepository;
import com.github.devsns.domain.notifications.service.NotificationService;
import com.github.devsns.domain.question.entity.QuestionBoardEntity;
import com.github.devsns.domain.question.repository.QuestionBoardRepository;
import com.github.devsns.domain.user.entitiy.UserEntity;
import com.github.devsns.domain.user.repository.UserRepository;
import com.github.devsns.global.constant.LikeType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AnswerServiceImpl implements AnswerService {

    private final AnswerRepository answerRepository;
    private final QuestionBoardRepository questionBoardRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;
    private final NotificationRepository notificationRepository;
    private final AnswerLikeRepository answerLikeRepository;
    private final AnswerCommentRepository answerCommentRepository;


    public void checkAnswerer(Long answerId, Long userId) {
        AnswerEntity answer = answerRepository.findById(answerId).orElseThrow(() -> new IllegalArgumentException("답변자가 아닙니다."));
        if (answer.getAnswerer().getUserId() != userId) {
            throw new RuntimeException("작성자가 아닙니다.");
        }

    }

    @Transactional
    public void createAnswer(Long quesId, Long userId, String userName, String content) {

        // 게시물 조회
        Optional<QuestionBoardEntity> question = questionBoardRepository.findById(quesId);

        // 질문 작성자
        UserEntity questionAuthor = question.get().getUser();

        // 답변 작성자
        Optional<UserEntity> answerer = userRepository.findById(userId);


        AnswerEntity answer = new AnswerEntity();
        answer.setContent(content);
        answer.setQuestionBoard(question.get());
        answer.setAnswerer(answerer.get());
        answerRepository.save(answer);

        notificationService.sendAnswerNotification(questionAuthor, answerer.get(), question.get());

    }


    @Transactional
    public String updateAnswerReaction(Long answerId, Long userId, LikeType likeType) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        AnswerEntity answer = answerRepository.findById(answerId)
                .orElseThrow(() -> new IllegalArgumentException("답변을 찾을 수 없습니다."));

        Optional<AnswerLike> existingLike = answerLikeRepository.findByAnswerIdAndUserId(answerId, user);

        if (existingLike.isPresent()) {
            if (existingLike.get().getLiketype() == likeType) {
                answerLikeRepository.delete(existingLike.get());
                notificationService.deleteLikeAnswerNotification(answer.getAnswerer().getUserId(), user.getUserId());
                return likeType == LikeType.LIKE ? "좋아요를 취소했습니다." : "싫어요를 취소했습니다.";
            } else {
                return "이미 다른 반응을 하셨습니다.";
            }
        } else {
            AnswerLike newLike = new AnswerLike();
            newLike.setUserId(user);
            newLike.setAnswer(answer);
            newLike.setLiketype(likeType);
            answerLikeRepository.save(newLike);
            if (likeType == LikeType.LIKE) {
                notificationService.sendLikeAnswerNotification(answer.getAnswerer(), user, answer);
            } else {
                notificationService.sendDislikeAnswerNotification(answer.getAnswerer(), user, answer);
            }
            return likeType == LikeType.LIKE ? "좋아요를 눌렀습니다." : "싫어요를 눌렀습니다.";
        }
    }
    @Transactional
    public List<Notification> deleteAnswer(Long answerId) {

        AnswerEntity answer = answerRepository.findById(answerId).orElseThrow(() -> new IllegalArgumentException("답변자가 아닙습니다."));
        List<Notification> notifications = notificationRepository.findAllByRecipientId(answer.getAnswerer().getUserId());

        // 모든 댓글과 좋아요에 대한 알림을 삭제합니다.
        for (Notification notification : notifications) {
            // 각 알림의 유형에 따라 알맞은 메소드를 호출하여 삭제합니다.
            if (notification.getType() == NotificationType.ANSWER_COMMENT) {
                notificationRepository.delete(notification);
            } else if (notification.getType() == NotificationType.ANSWER_LIKE) {
                notificationRepository.delete(notification);
            } else if (notification.getType() == NotificationType.ANSWER_DISLIKE) {
                notificationRepository.delete(notification);
            }
        }

        List<AnswerLike> likes = answer.getLikes();
        for (AnswerLike like : likes) {
            answerLikeRepository.delete(like);
        }

        List<AnswerCommentEntity> comments = answer.getComments();
        for (AnswerCommentEntity comment : comments) {
            answerCommentRepository.delete(comment);
        }

        notificationService.deleteAnswerNotification(answerId, answer.getAnswerer().getUserId());
        answerRepository.delete(answer);
        return notifications;
    }

    @Transactional
    public void updateAnswer(Long answerId, Long userId, String content) {

        AnswerEntity answer = answerRepository.findById(answerId).orElseThrow(() -> new IllegalArgumentException("답변자가 아닙습니다."));
        if (answer.getAnswerer().getUserId() != userId) {
            throw new RuntimeException("작성자가 아닙니다.");
        }
        answer.setContent(content);
        answerRepository.save(answer);
    }
}




