package com.github.devsns.domain.comments.service;

import com.github.devsns.domain.comments.entity.QuestionCommentEntity;
import com.github.devsns.domain.comments.repository.QuestionCommentRepository;
import com.github.devsns.domain.notifications.service.NotificationService;
import com.github.devsns.domain.question.entity.QuestionBoardEntity;
import com.github.devsns.domain.question.repository.QuestionBoardRepository;
import com.github.devsns.domain.question.service.QuestionBoardService;
import com.github.devsns.domain.user.entitiy.UserEntity;
import com.github.devsns.domain.user.repository.UserRepository;
import com.github.devsns.domain.user.service.UserService;
import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToMany;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class QuestionCommentServiceImpl implements QuestionCommentService {
    private final QuestionCommentRepository questionCommentRepository;
    private final NotificationService notificationService;
    private final UserService userService;
    private final QuestionBoardService questionBoardService;
    private final UserRepository userRepository;
    private final QuestionBoardRepository questionBoardRepository;


    public QuestionCommentServiceImpl(QuestionCommentRepository questionCommentRepository, NotificationService notificationService, UserService userService, QuestionBoardService questionBoardService,
                                      UserRepository userRepository, QuestionBoardRepository questionBoardRepository) {
        this.questionCommentRepository = questionCommentRepository;
        this.notificationService = notificationService;
        this.userService = userService;
        this.questionBoardService = questionBoardService;
        this.questionBoardRepository = questionBoardRepository;
        this.userRepository = userRepository;
    }


    public void checkCommenter(String commentId, Long userId) {
        QuestionCommentEntity comment = questionCommentRepository.findById(Long.parseLong(commentId)).orElseThrow(() -> new IllegalArgumentException("코멘터가 아닙니다."));
        if (comment.getCommenter().getUserId() != userId) {
            throw new IllegalArgumentException("작성자가 아닙니다.");
        }
    }
    @Transactional
    public void createQuestionComment(Long quesId, Long userId, String content) {

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


        QuestionCommentEntity savedComment = questionCommentRepository.save(comment);

        // 게시물 작성자에게 알림 전송
        notificationService.sendCommentNotification(questionAuthor, savedComment);
    }
    @Transactional
    public void updateQuestionComment(String commentId, String content) {
        QuestionCommentEntity comment = questionCommentRepository.findById(Long.parseLong(commentId)).orElseThrow(() -> new NoSuchElementException("해당 댓글을 찾을 수 없습니다."));
        comment.setContent(content);
        comment.setUpdatedAt(LocalDateTime.now());
        questionCommentRepository.save(comment);
    }

    @Transactional
    public void deleteQuestionComment(String commentId) {
        QuestionCommentEntity comment = questionCommentRepository.findById(Long.parseLong(commentId)).orElseThrow(() -> new NoSuchElementException("해당 댓글을 찾을 수 없습니다."));

        notificationService.deleteQuestionCommentNotification(comment);

        questionCommentRepository.delete(comment);
    }

//    public List<QuestionCommentEntity> getAllComments(Long quesId) {
//        Optional<QuestionBoardEntity> questionOptional = questionBoardRepository.findById(quesId);
//        if (questionOptional.isPresent()) {
//            QuestionBoardEntity question = questionOptional.get();
//            return question.getComments();
//        } else {
//            // 해당 ID에 해당하는 질문이 없을 경우 처리할 내용 추가
//            throw new NoSuchElementException("해당 ID에 해당하는 질문이 없습니다.");
//        }
//    }

//  저기 동근님 entity에
//    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
//    private final List<QuestionCommentEntity> comments = new ArrayList<>();
}

