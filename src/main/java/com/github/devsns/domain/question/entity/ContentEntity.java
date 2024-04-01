package com.github.devsns.domain.question.entity;

import com.github.devsns.domain.question.dto.QuestionBoardReqDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "content")
public class ContentEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "content_id")
    private Long id;

    @Column(name = "markdown_content")
    private String content;

    public static ContentEntity toEntity(String content) {
        return ContentEntity.builder()
                .content(content)
                .build();
    }
}
