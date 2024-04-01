package com.github.devsns.domain.qnas.entity;

import jakarta.persistence.*;

@Entity(name = "content")
public class ContentEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "content_id")
    private Long id;
}
