//package com.github.devsns.domain.chat.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.Customizer;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.web.SecurityFilterChain;
//
//@Configuration
//public class SecurityConfig {
//
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http
//                .csrf(csrf -> csrf.disable())  // CSRF 보호 비활성화
//                .authorizeHttpRequests((authz) -> authz
//                        .requestMatchers("/**").permitAll()  // 특정 경로 인증 없이 접근 허용
//                        .anyRequest().authenticated())  // 나머지 요청은 인증 필요
//                .formLogin(Customizer.withDefaults());  // 기본 로그인 페이지 제공
//
//        return http.build();
//    }
//}
