package com.github.devsns.domain.role.roleRepository;

import com.github.devsns.domain.role.roleEntity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, Long> {
}
