package com.github.devsns.domain.answers.service;

import com.github.devsns.domain.notifications.entity.Notification;

import java.util.List;

public interface AnswerService {


    void checkAnswerer(Long answerId, Long userId);

    void createAnswer(Long quesId, Long userId, String title, String content);

    void likeAnswer(Long answerId, Long userId);

    void unlikeAnswer(Long answerId, Long userId);

    List<Notification> deleteAnswer(Long answerId);

    void updateAnswer(Long answerId, Long userId, String title, String content);

}
