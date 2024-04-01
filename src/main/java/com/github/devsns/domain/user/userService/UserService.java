package com.github.devsns.domain.user.userService;

import com.github.devsns.domain.user.userDto.SignupDto;
import com.github.devsns.domain.user.userEntities.UserEntity;
import com.github.devsns.domain.user.userRepository.UserRepository;
import com.github.devsns.exception.AppException;
import com.github.devsns.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

   @Transactional
    public void signUp(SignupDto signupDto){
       // 입력한 값들 받아오기
       UserEntity userEntity = UserEntity.builder()
               .email(signupDto.getEmail())
               .password(passwordEncoder.encode(signupDto.getPassword()))
               .username(signupDto.getUsername())
               .createAt(LocalDateTime.now())
               .build();

       // 권한 부여
       userEntity.addUserAuthority();

       // 이메일 중복 체크
       if (userRepository.findByEmail(signupDto.getEmail()).isPresent()){
           throw new AppException(ErrorCode.EMAIL_DUPLICATED.getMessage(), ErrorCode.EMAIL_DUPLICATED);
       }

       // 유저네임 중복체크
       if (userRepository.findByUsername(signupDto.getUsername()).isPresent()){
           throw new AppException(ErrorCode.USERNAME_DUPLICATED.getMessage(), ErrorCode.USERNAME_DUPLICATED);
       }

       // 회원정보 저장
       userRepository.save(userEntity);
   }

}
