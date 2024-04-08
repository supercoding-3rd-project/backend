package com.github.devsns.domain.answers.service;

import com.github.devsns.domain.answers.dto.AnswerRequest;

public interface AnswerService {


    void checkAnswerer(Long answerId, Long userId);

    void createAnswer(Long quesId, Long userId, String title, String content);

    void likeAnswer(Long answerId, Long userId);

    void unlikeAnswer(Long answerId, Long userId);

    void deleteAnswer(Long answerId);

    void updateAnswer(Long answerId, Long userId, String title, String content);

}
