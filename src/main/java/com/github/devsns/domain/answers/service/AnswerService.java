package com.github.devsns.domain.answers.service;

import com.github.devsns.domain.notifications.entity.Notification;
import com.github.devsns.global.constant.LikeType;

import java.util.List;

public interface AnswerService {


    void checkAnswerer(Long answerId, Long userId);

    void createAnswer(Long quesId, Long userId, String userName, String content);

    String updateAnswerReaction(Long answerId, Long userId, LikeType likeType);

    List<Notification> deleteAnswer(Long answerId);

    void updateAnswer(Long answerId, Long userId, String content);

}
