package com.github.devsns.domain.oauth2.handler;

import com.github.devsns.domain.oauth2.CustomOAuth2User;
import com.github.devsns.domain.user.entitiy.Role;
import com.github.devsns.domain.user.repository.UserRepository;
import com.github.devsns.global.jwt.service.JwtService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtService jwtService;
    private final UserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("OAuth2 Login 성공!");
        try {
            CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();

            if(oAuth2User.getRole() == Role.USER) {
                String accessToken = jwtService.createAccessToken(oAuth2User.getEmail());
                response.addHeader(jwtService.getAccessHeader(), "Bearer " + accessToken);

                response.sendRedirect("oauth2/sign-up"); // 프론트의 회원가입 추가 정보 입력 폼으로 리다이렉트


                jwtService.sendAccessAndRefreshToken(response, accessToken, null);

            } else {
                loginSuccess(response, oAuth2User); // 로그인에 성공한 경우 access, refresh 토큰 생성
            }
        } catch (Exception e) {
            throw e;
        }
    }

    private void loginSuccess(HttpServletResponse response, CustomOAuth2User oAuth2User) throws IOException {
        String accessToken = jwtService.createAccessToken(oAuth2User.getEmail());
        String refreshToken = jwtService.createRefreshToken();
        response.addHeader(jwtService.getAccessHeader(), "Bearer " + accessToken);
        response.addHeader(jwtService.getRefreshHeader(), "Bearer " + refreshToken);

        jwtService.sendAccessAndRefreshToken(response, accessToken, refreshToken);
        jwtService.updateRefreshToken(oAuth2User.getEmail(), refreshToken);
    }
}
