package com.github.devsns.domain.user.repository;

import com.github.devsns.domain.user.entitiy.SocialType;
import com.github.devsns.domain.user.entitiy.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@EnableJpaRepositories
@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByEmail(String email);

    Optional<UserEntity> findByUsername(String username);

    Optional<UserEntity> findByRefreshToken(String refreshToken);

    Optional<UserEntity> findBySocialTypeAndSocialId(SocialType socialType, String socialId);

    Page<UserEntity> findByUsernameContaining(String keyword, Pageable pageable);
    }

