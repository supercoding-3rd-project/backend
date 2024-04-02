package com.github.devsns.domain.answers.service;

import com.github.devsns.domain.answers.dto.AnswerRequest;
import com.github.devsns.domain.answers.entity.AnswerEntity;
import com.github.devsns.domain.answers.repository.AnswerRepository;
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

    public AnswerServiceImpl(AnswerRepository answerRepository, QuestionBoardRepository questionBoardRepository, UserRepository userRepository) {
        this.userRepository = userRepository;
        this.questionBoardRepository = questionBoardRepository;
        this.answerRepository = answerRepository;
    }

    public void checkAnswerer(Long answerId, Long userId) {
        AnswerEntity answer = answerRepository.findById(answerId).orElseThrow(() -> new IllegalArgumentException("답변자가 아닙니다."));
        if (answer.getAnswerer().getUserId() != userId) {
            throw new RuntimeException("작성자가 아닙니다.");
        }

    }

    ;

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

    }

    ;

    public void likeAnswer(Long answerId, Long userId) {
    }

    ;

    public void unlikeAnswer(Long answerId, Long userId) {
    }

    ;

    public void deleteAnswer(Long answerId) {
    }

    ;

    public void updateAnswer(Long answerId, Long userId, String title, String content) {
    }

    ;


}
