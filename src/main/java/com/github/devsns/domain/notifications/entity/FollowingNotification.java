//package com.github.devsns.domain.notifications.entity;
//
//import com.github.devsns.domain.user.entitiy.UserEntity;
//import jakarta.persistence.*;
//import lombok.Data;
//import org.springframework.format.annotation.DateTimeFormat;
//
//import java.time.LocalDateTime;
//
//@Entity
//@Data
//public class FollowingNotification {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//    @ManyToOne
//    private UserEntity recipient;
//    @ManyToOne
//    private UserEntity follower;
//
//    @Column(nullable = false, updatable = false)
//    @DateTimeFormat(pattern = "yy.mm.dd hh:mm")
//    private LocalDateTime createdAt;
//
//}
