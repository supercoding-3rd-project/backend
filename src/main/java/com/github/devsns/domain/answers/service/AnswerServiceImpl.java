package com.github.devsns.domain.answers.service;

import com.github.devsns.domain.answers.entity.AnswerEntity;
import com.github.devsns.domain.answers.entity.AnswerLike;
import com.github.devsns.domain.answers.repository.AnswerRepository;
import com.github.devsns.domain.notifications.service.NotificationService;
import com.github.devsns.domain.question.entity.QuestionBoardEntity;
import com.github.devsns.domain.question.repository.QuestionBoardRepository;
import com.github.devsns.domain.user.entitiy.UserEntity;
import com.github.devsns.domain.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AnswerServiceImpl implements AnswerService {

    private final AnswerRepository answerRepository;
    private final QuestionBoardRepository questionBoardRepository;
    private final UserRepository userRepository;

    private final NotificationService notificationService;

    public AnswerServiceImpl(AnswerRepository answerRepository, QuestionBoardRepository questionBoardRepository, UserRepository userRepository, NotificationService notificationService
    ) {
        this.userRepository = userRepository;
        this.questionBoardRepository = questionBoardRepository;
        this.answerRepository = answerRepository;
        this.notificationService = notificationService;
    }

    public void checkAnswerer(Long answerId, Long userId) {
        AnswerEntity answer = answerRepository.findById(answerId).orElseThrow(() -> new IllegalArgumentException("답변자가 아닙니다."));
        if (answer.getAnswerer().getUserId() != userId) {
            throw new RuntimeException("작성자가 아닙니다.");
        }

    }

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
        answer.setQuestion(question.get());
        answer.setAnswerer(answerer.get());
        answerRepository.save(answer);

        notificationService.sendAnswerNotification(questionAuthor, answerer.get(), question.get());

    }


    public void likeAnswer(Long answerId, Long userId) {

        AnswerEntity answer = answerRepository.findById(answerId).orElseThrow(() -> new IllegalArgumentException("답변자가 아닙습니다."));
        Optional<UserEntity> user = userRepository.findById(userId);

        // 새로운 좋아요 엔티티 생성
        AnswerLike like = new AnswerLike();
        like.setUser(user.get());
        like.setAnswerLike(answer);

        // 답변의 좋아요 리스트에 추가
        answer.getLikes().add(like);


        // 좋아요 저장
        answerRepository.save(answer);

        notificationService.sendLikeAnswerNotification(user.get(), answer.getAnswerer(), answer);
    }


    public void unlikeAnswer(Long answerId, Long userId) {
        AnswerEntity answer = answerRepository.findById(answerId).orElseThrow(() -> new IllegalArgumentException("답변자가 아닙습니다."));

        // 사용자가 해당 답변에 좋아요를 눌렀는지 확인하고 좋아요 취소
        answer.getLikes().removeIf(like -> like.getUser().getUserId().equals(userId));

        //좋아요 상태 반영
        answerRepository.save(answer);
    }


    public void deleteAnswer(Long answerId) {

        AnswerEntity answer = answerRepository.findById(answerId).orElseThrow(() -> new IllegalArgumentException("답변자가 아닙습니다."));
        answerRepository.delete(answer);
    }


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
