package com.github.devsns.domain.comments.service;

import com.github.devsns.domain.answers.entity.AnswerEntity;
import com.github.devsns.domain.answers.repository.AnswerRepository;
import com.github.devsns.domain.comments.constant.CommentType;
import com.github.devsns.domain.comments.entity.AnswerCommentEntity;
import com.github.devsns.domain.comments.entity.QuestionCommentEntity;
import com.github.devsns.domain.comments.entity.QuestionLikeComment;
import com.github.devsns.domain.comments.repository.AnswerCommentRepository;
import com.github.devsns.domain.comments.repository.QuestionCommentRepository;
import com.github.devsns.domain.notifications.service.NotificationService;
import com.github.devsns.domain.question.entity.QuestionBoardEntity;
import com.github.devsns.domain.question.repository.QuestionBoardRepository;
import com.github.devsns.domain.question.service.QuestionBoardService;
import com.github.devsns.domain.user.entitiy.UserEntity;
import com.github.devsns.domain.user.repository.UserRepository;
import com.github.devsns.domain.user.service.UserService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class CommentServiceImpl implements CommentService {
    private final QuestionCommentRepository questionCommentRepository;
    private final NotificationService notificationService;
    private final UserService userService;
    private final QuestionBoardService questionBoardService;
    private final UserRepository userRepository;
    private final QuestionBoardRepository questionBoardRepository;
    private final AnswerRepository answerRepository;
    private final AnswerCommentRepository answerCommentRepository;

    public CommentServiceImpl(QuestionCommentRepository questionCommentRepository, NotificationService notificationService, UserService userService, QuestionBoardService questionBoardService,
                              UserRepository userRepository, QuestionBoardRepository questionBoardRepository,
                              AnswerRepository answerRepository,
                              AnswerCommentRepository answerCommentRepository) {
        this.questionCommentRepository = questionCommentRepository;
        this.notificationService = notificationService;
        this.userService = userService;
        this.questionBoardService = questionBoardService;
        this.questionBoardRepository = questionBoardRepository;
        this.userRepository = userRepository;
        this.answerRepository = answerRepository;
        this.answerCommentRepository = answerCommentRepository;
    }


    public void checkCommenter(String commentId, Long userId) {
        QuestionCommentEntity comment = questionCommentRepository.findById(Long.parseLong(commentId)).orElseThrow(() -> new IllegalArgumentException("코멘터가 아닙니다."));
        if (comment.getCommenter().getUserId() != userId) {
            throw new IllegalArgumentException("작성자가 아닙니다.");
        }
    }

    public void createQuestionComment(Long quesId, Long userId, String content, Long parentCommentId) {

        // 게시물 조회
        Optional<QuestionBoardEntity> question = questionBoardRepository.findById(quesId);
        //QuestionBoardEntity post = questionBoardService.getPost(postId);

        // 질문 작성자
        UserEntity questionAuthor = question.get().getUser();

        // 댓글 작성자
        Optional<UserEntity> commenter = userRepository.findById(userId);
        //UserEntity commenter = userService.getUser(userId);


        QuestionCommentEntity comment = new QuestionCommentEntity();
        comment.setContent(content);
        comment.setQuestion(question.get());
        comment.setCommenter(commenter.get());

        if (parentCommentId == null) {
            // 최상위 댓글
            comment.setParentComment(null);
            comment.setType(CommentType.COMMENT);
        } else {
            // 대댓글
            QuestionCommentEntity parentComment = questionCommentRepository.findById(parentCommentId).orElseThrow(() -> new NoSuchElementException("해당 ID에 해당하는 부모 댓글을 찾을 수 없습니다."));
            comment.setParentComment(parentComment);
            comment.setType(CommentType.REPLY);
        }

        QuestionCommentEntity savedComment = questionCommentRepository.save(comment);

        // 게시물 작성자에게 알림 전송
        notificationService.sendCommentNotification(questionAuthor, savedComment);
    }


    public void createAnswerComment(Long answerId, Long userId, String content, Long parentCommentId) {

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

        if (parentCommentId == null) {
            // 최상위 댓글
            comment.setParentComment(null);
            comment.setType(CommentType.COMMENT);
        } else {
            // 대댓글
            AnswerCommentEntity parentComment = answerCommentRepository.findById(parentCommentId).orElseThrow(() -> new NoSuchElementException("해당 ID에 해당하는 부모 댓글을 찾을 수 없습니다."));
            comment.setParentComment(parentComment);
            comment.setType(CommentType.REPLY);
        }

        AnswerCommentEntity savedComment = answerCommentRepository.save(comment);

        // 게시물 작성자에게 알림 전송
        notificationService.sendCommentNotification(answerer, savedComment);
    }





    public void updateComment(String commentId, String content) {
        QuestionCommentEntity comment = questionCommentRepository.findById(Long.parseLong(commentId)).orElseThrow(() -> new NoSuchElementException("해당 댓글을 찾을 수 없습니다."));
        comment.setContent(content);
        comment.setUpdatedAt(LocalDateTime.now());
        questionCommentRepository.save(comment);
    }


    public void deleteComment(String commentId) {
        QuestionCommentEntity comment = questionCommentRepository.findById(Long.parseLong(commentId)).orElseThrow(() -> new NoSuchElementException("해당 댓글을 찾을 수 없습니다."));
        questionCommentRepository.delete(comment);
    }


    public void likeComment(String commentId, Long userId) {
        QuestionCommentEntity comment = questionCommentRepository.findById(Long.parseLong(commentId)).orElseThrow(() -> new NoSuchElementException("해당 댓글을 찾을 수 없습니다."));
        Optional<UserEntity> user = userRepository.findById(userId);

        // 새로운 좋아요 엔티티 생성
        QuestionLikeComment like = new QuestionLikeComment();
        like.setUser(user.get());
        like.setQuestionComment(comment);

        // 댓글의 좋아요 리스트에 추가
        comment.getLikes().add(like);

        // 댓글 저장
        questionCommentRepository.save(comment);

    }


    public void unlikeComment(String commentId, Long userId) {
        QuestionCommentEntity comment = questionCommentRepository.findById(Long.parseLong(commentId)).orElseThrow(() -> new NoSuchElementException("해당 댓글을 찾을 수 없습니다."));

        // 사용자가 해당 댓글에 좋아요를 눌렀는지 확인하고 좋아요 취소
        comment.getLikes().removeIf(like -> like.getUser().getUserId().equals(userId));

        // 댓글 저장
        questionCommentRepository.save(comment);

    }

}


