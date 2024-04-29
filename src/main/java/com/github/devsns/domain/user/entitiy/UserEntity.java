package com.github.devsns.domain.user.entitiy;

import com.github.devsns.domain.answers.entity.AnswerEntity;
import com.github.devsns.domain.follow.entity.FollowEntity;
import com.github.devsns.domain.question.entity.QuestionBoardEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class UserEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    private String email;
    private String password;
    private String username;
    private String imageUrl;
    private String description;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Enumerated(EnumType.STRING)
    private SocialType socialType;

    private String socialId;

    private String refreshToken;

    private LocalDateTime createdAt;

    // 질문
    @OneToMany(mappedBy = "questioner", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<QuestionBoardEntity> questionBoardEntities;

    // 답변
    @OneToMany(mappedBy = "answerer", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<AnswerEntity> answerEntities;

    // 팔로우
    @OneToMany(mappedBy = "fromUser", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<FollowEntity> followings;

    @OneToMany(mappedBy = "toUser", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<FollowEntity> followers;

    // 비밀번호 암호화
    public void passwordEncode(PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(this.password);
    }

    // 리프레시 토큰 업데이트
    public void updateRefreshToken(String updateRefreshToken) {
        this.refreshToken = updateRefreshToken;
    }
}
