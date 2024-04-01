package com.github.devsns.domain.user.userRespository;

import com.github.devsns.domain.user.userEntity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface userRepository extends JpaRepository<UserEntity, Long> {

}
