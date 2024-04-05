package com.github.devsns.domain.comments.service;

import com.github.devsns.domain.answers.entity.AnswerEntity;
import com.github.devsns.domain.answers.repository.AnswerRepository;
import com.github.devsns.domain.comments.entity.AnswerCommentEntity;
import com.github.devsns.domain.comments.repository.AnswerCommentRepository;
import com.github.devsns.domain.notifications.service.NotificationService;
import com.github.devsns.domain.user.entitiy.UserEntity;
import com.github.devsns.domain.user.repository.UserRepository;
import com.github.devsns.domain.user.service.UserService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class AnswerCommentServiceImpl implements AnswerCommentService {
    private final NotificationService notificationService;
    private final UserService userService;
    private final UserRepository userRepository;
    private final AnswerRepository answerRepository;
    private final AnswerCommentRepository answerCommentRepository;


    public AnswerCommentServiceImpl(NotificationService notificationService, UserService userService,
                                    UserRepository userRepository,
                                    AnswerRepository answerRepository,
                                    AnswerCommentRepository answerCommentRepository) {
        this.notificationService = notificationService;
        this.userService = userService;
        this.userRepository = userRepository;
        this.answerRepository = answerRepository;
        this.answerCommentRepository = answerCommentRepository;
    }

    public void checkCommenter(String commentId, Long userId) {
        AnswerCommentEntity comment = answerCommentRepository.findById(Long.parseLong(commentId)).orElseThrow(() -> new IllegalArgumentException("코멘터가 아닙니다."));
        if (comment.getCommenter().getUserId() != userId) {
            throw new IllegalArgumentException("작성자가 아닙니다.");
        }
    }

    public void createAnswerComment(Long answerId, Long userId, String content) {

        // 게시물 조회
        Optional<AnswerEntity> answer = answerRepository.findById(answerId);
        //QuestionBoardEntity post = questionBoardService.getPost(postId);


        // 답변 작성자
        UserEntity answerer = answer.get().getAnswerer();


        // 댓글 작성자
        Optional<UserEntity> commenter = userRepository.findById(userId);
        //UserEntity commenter = userService.getUser(userId);


        AnswerCommentEntity comment = new AnswerCommentEntity();
        comment.setContent(content);
        comment.setAnswer(answer.get());
        comment.setCommenter(commenter.get());


        AnswerCommentEntity savedComment = answerCommentRepository.save(comment);

        // 게시물 작성자에게 알림 전송
        notificationService.sendCommentNotification(answerer, savedComment);
    }


    public void updateAnswerComment(String commentId, String content) {
        AnswerCommentEntity comment = answerCommentRepository.findById(Long.parseLong(commentId)).orElseThrow(() -> new NoSuchElementException("해당 댓글을 찾을 수 없습니다."));
        comment.setContent(content);
        comment.setUpdatedAt(LocalDateTime.now());
        answerCommentRepository.save(comment);
    }


    public void deleteAnswerComment(String commentId) {
        AnswerCommentEntity comment = answerCommentRepository.findById(Long.parseLong(commentId)).orElseThrow(() -> new NoSuchElementException("해당 댓글을 찾을 수 없습니다."));
        answerCommentRepository.delete(comment);
    }


    public List<AnswerCommentEntity> getAllComments(Long quesId) {
        Optional<AnswerEntity> answer = answerRepository.findById(quesId);
        return answer.get().getComments();
    }

}

