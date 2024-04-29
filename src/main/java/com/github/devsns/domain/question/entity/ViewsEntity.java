package com.github.devsns.domain.question.entity;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Table(name = "views")
public class ViewsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "view_id")
    private Long id;

    private Long userId;
    private Long questionId;

    @CreatedDate
    private LocalDateTime viewedAt;

}
