package com.github.devsns.domain.user.repository;

import com.github.devsns.domain.user.entitiy.UserRoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRoleRepository extends JpaRepository<UserRoleEntity, Long> {
}
