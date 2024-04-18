package com.github.devsns.domain.question.dto.search;


import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@RequiredArgsConstructor
public class SearchData {

    private int currentPage;
    private int totalPages;
    private int pageSize;
    private long totalItems;
    private List<SearchQuestionDto> searchQuestionDto;
    private List<SearchUserDto> searchUserDto;
}