package com.github.devsns.domain.userDetails.service;

import com.github.devsns.domain.role.entity.RoleEntity;
import com.github.devsns.domain.user.entitiy.UserEntity;
import com.github.devsns.domain.user.entitiy.UserRoleEntity;
import com.github.devsns.domain.user.repository.UserRepository;
import com.github.devsns.domain.userDetails.entity.CustomUserDetails;
import com.github.devsns.exception.AppException;
import com.github.devsns.exception.ErrorCode;
import lombok.RequiredArgsConstructor;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.management.relation.Role;


@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByEmail(email).orElseThrow(
                () -> new AppException(ErrorCode.USER_EMAIL_NOT_FOUND.getMessage(), ErrorCode.USER_EMAIL_NOT_FOUND)
        );

        return CustomUserDetails.builder()
                .userId(userEntity.getUserId())
                .email(userEntity.getEmail())
                .password(userEntity.getPassword())
                .authorities(userEntity.getRoles().stream().map(UserRoleEntity::getRole).map(RoleEntity::getRoleName).toList())
                .build();
    }
}
