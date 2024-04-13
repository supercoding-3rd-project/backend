package com.github.devsns.domain.question.util;

import com.github.devsns.domain.question.dto.QuestionBoardResDto;
import com.github.devsns.domain.user.dto.UserResponseDto;

import java.util.List;

public class SearchResult {
    private List<QuestionBoardResDto> questionBoards;
    private List<UserResponseDto> users;

    // 생성자
    public SearchResult(List<QuestionBoardResDto> questionBoards, List<UserResponseDto> users) {
        this.questionBoards = questionBoards;
        this.users = users;
    }

    // Getter
    public List<QuestionBoardResDto> getQuestionBoards() {
        return questionBoards;
    }

    public List<UserResponseDto> getUsers() {
        return users;
    }
}