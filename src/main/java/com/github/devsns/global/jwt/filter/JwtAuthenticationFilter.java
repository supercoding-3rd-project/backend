package com.github.devsns.global.jwt.filter;

import com.github.devsns.domain.auth.entity.CustomUserDetails;
import com.github.devsns.domain.user.entitiy.UserEntity;
import com.github.devsns.domain.user.repository.UserRepository;
import com.github.devsns.global.jwt.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String NO_CHECK_URL = "/api/login"; // "/login"으로 들어오는 요청은 Filter 작동 X
    private final JwtService jwtService;
    private final UserRepository userRepository;

    private GrantedAuthoritiesMapper authoritiesMapper = new NullAuthoritiesMapper();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        /*
        로그인 요청이 들어오면 필터 통과하고 다음 호출로 넘어간다.
        이때, return을 통해 다음 필터를 호출하고 현재 필터의 진행을 막는다.
         */
        if (request.getRequestURI().equals(NO_CHECK_URL)) {
            filterChain.doFilter(request, response);
            return;
        }

        /*
        헤더에서 리프레시 토큰 추출한다.
        만약 토큰이 없다면 null을 반환
         */
        String refreshToken = jwtService.extractRefreshToken(request)
                .filter(jwtService::isTokenValid)
                .orElse(null);

        if (refreshToken != null) {
            checkRefreshTokenAndReIssueAccessToken(response, refreshToken);
            return; // RefreshToken을 보낸 경우에는 AccessToken을 재발급 하고 인증 처리는 하지 않게 하기위해 바로 return으로 필터 진행 막기
        } else {
            checkAccessTokenAndAuthentication(request, response, filterChain);
        }
    }

    /*
    [리프레시 토큰으로 유저 정보 찾기 & 액세스 토큰/리프레시 토큰 재발급 메소드]
    파라미터로 들어온 헤더에서 추출한 리프레시 토큰으로 DB에서 유저를 찾고, 해당 유저가 있다면
    JwtService.createAccessToken()으로 AccessToken 생성,
    reIssueRefreshToken()로 리프레시 토큰 재발급 & DB에 리프레시 토큰 업데이트 메소드 호출
    그 후 JwtService.sendAccessTokenAndRefreshToken()으로 응답 헤더에 보내기
    */
    public void checkRefreshTokenAndReIssueAccessToken(HttpServletResponse response, String refreshToken) {
        userRepository.findByRefreshToken(refreshToken)
                .ifPresent(userEntity -> {
                    String reIssuedRefreshToken = reIssueRefreshToken(userEntity);
                    jwtService.sendAccessAndRefreshToken(response, jwtService.createAccessToken(userEntity.getEmail()),
                            reIssuedRefreshToken);
                });
    }

    /*
    [리프레시 토큰 재발급 & DB에 리프레시 토큰 업데이트 메소드]
    jwtService.createRefreshToken()으로 리프레시 토큰 재발급 후
    DB에 재발급한 리프레시 토큰 업데이트 후 Flush
    */
    private String reIssueRefreshToken(UserEntity user) {
        String reIssuedRefreshToken = jwtService.createRefreshToken();
        user.updateRefreshToken(reIssuedRefreshToken);
        userRepository.saveAndFlush(user);
        return reIssuedRefreshToken;
    }

    /*
    [액세스 토큰 체크 & 인증 처리 메소드]
    request에서 extractAccessToken()으로 액세스 토큰 추출 후, isTokenValid()로 유효한 토큰인지 검증
    유효한 토큰이면, 액세스 토큰에서 extractEmail로 Email을 추출한 후 findByEmail()로 해당 이메일을 사용하는 유저 객체 반환
    그 유저 객체를 saveAuthentication()으로 인증 처리하여
    인증 허가 처리된 객체를 SecurityContextHolder에 담기
    그 후 다음 인증 필터로 진행
    */
    public void checkAccessTokenAndAuthentication(HttpServletRequest request, HttpServletResponse response,
                                                  FilterChain filterChain) throws ServletException, IOException {
        jwtService.extractAccessToken(request)
                .filter(jwtService::isTokenValid)
                .ifPresent(accessToken -> jwtService.extractEmail(accessToken)
                        .ifPresent(email -> userRepository.findByEmail(email)
                                .ifPresent(this::saveAuthentication)));

        filterChain.doFilter(request, response);
    }

    /*
    UserDetails의 User를 Builder로 생성 후 해당 객체를 인증 처리하여
    해당 유저 객체를 SecurityContextHolder에 담아 인증 처리를 진행
    */
    public void saveAuthentication(UserEntity user) {
        String password = user.getPassword();
//        if (password == null) { // 소셜 로그인 유저의 비밀번호 임의로 설정 하여 소셜 로그인 유저도 인증 되도록 설정
//            password = PasswordUtil.generateRandomPassword();
//        }

        CustomUserDetails customUserDetails = CustomUserDetails.builder()
                .userEntity(user)
                .build();

        Authentication authentication =
                new UsernamePasswordAuthenticationToken(customUserDetails, null,
                        authoritiesMapper.mapAuthorities(customUserDetails.getAuthorities()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
