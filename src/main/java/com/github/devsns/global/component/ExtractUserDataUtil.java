package com.github.devsns.global.component;

import com.github.devsns.domain.user.entitiy.UserEntity;
import com.github.devsns.domain.user.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ExtractUserDataUtil {
    private UserRepository userRepository;

    public ExtractUserDataUtil(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Long extractUserIdFromAuthentication(Authentication authentication) {
        String email =  authentication.getName();
        Optional<UserEntity> user = userRepository.findByEmail(email);
        long userId = user.get().getUserId();
        return userId;
    }

    public String extractUserNameFromAuthentication(Authentication authentication) {
        String email =  authentication.getName();
        Optional<UserEntity> user = userRepository.findByEmail(email);
        String userName = user.get().getUsername();
        return userName;
    }

}
