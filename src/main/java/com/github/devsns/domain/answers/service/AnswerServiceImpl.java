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
    public void createAnswer(Long quesId, Long userId, String title, String content) {

        // 게시물 조회
        Optional<QuestionBoardEntity> question = questionBoardRepository.findById(quesId);

        // 질문 작성자
        UserEntity questionAuthor = question.get().getUser();

        // 답변 작성자
        Optional<UserEntity> answerer = userRepository.findById(userId);


        AnswerEntity answer = new AnswerEntity();
        answer.setContent(content);
        answer.setTitle(title);
        answer.setQuestionBoard(question.get());
        answer.setAnswerer(answerer.get());
        answerRepository.save(answer);

        notificationService.sendAnswerNotification(questionAuthor, answerer.get(), question.get());

    }


    @Transactional
    public String likeAnswer(Long answerId, Long userId) {

        AnswerEntity answer = answerRepository.findById(answerId).orElseThrow(() -> new IllegalArgumentException("답변자가 아닙습니다."));
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        boolean isLiked = false;

        for (AnswerLike existingLike : answer.getLikes()) {
            if (existingLike.getUserId().getUserId().equals(userId)) {
                // 좋아요 취소
                answer.getLikes().remove(existingLike);
                isLiked = true;
                break;
            }
        }

        // 새로운 좋아요 엔티티 생성
        if (!isLiked) {
            AnswerLike like = new AnswerLike();
            like.setUserId(user);
            like.setAnswer(answer);

            // 답변의 좋아요 리스트에 추가
            answer.getLikes().add(like);
            // 좋아요 저장
            answerRepository.save(answer);
            notificationService.sendLikeAnswerNotification(answer.getAnswerer(), user, answer);
        }
        answerRepository.save(answer);
        // 좋아요 상태에 따라 응답 제공
        return isLiked ? "좋아요를 취소했습니다." : "좋아요를 눌렀습니다.";
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
    public void updateAnswer(Long answerId, Long userId, String title, String content) {

        AnswerEntity answer = answerRepository.findById(answerId).orElseThrow(() -> new IllegalArgumentException("답변자가 아닙습니다."));
        if (answer.getAnswerer().getUserId() != userId) {
            throw new RuntimeException("작성자가 아닙니다.");
        }
        answer.setTitle(title);
        answer.setContent(content);
        answerRepository.save(answer);
    }
}




