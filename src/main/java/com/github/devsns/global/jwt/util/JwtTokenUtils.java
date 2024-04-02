package com.github.devsns.global.jwt.util;

import com.github.devsns.exception.AppException;
import com.github.devsns.exception.ErrorCode;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;
import static java.lang.System.getenv;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenUtils {

    // 환경변수에 저장된 값 가져오기
    private final Map<String, String> env = getenv();
    private Date now = new Date();
    private final UserDetailsService userDetailsService;

    // 액세스 토큰 생성
    public String createAccessToken (String email) {

        return Constants.BEARER + Jwts.builder()
                .setIssuer(env.get("ISSUER"))
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + Constants.ACCESSTOKENVAILIDMILLISECOND))
                .claim("email", email)
                .signWith(SignatureAlgorithm.HS256, env.get("SECRET_KEY"))
                .compact();

    }

    // 리프레시 토큰 생성
    public String createRefreshToken (String email) {

        return Jwts.builder()
                .setIssuer(env.get("ISSUER"))
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + Constants.REFRESHTOKENVAILIDMILLISECOND))
                .claim("email", email)
                .signWith(SignatureAlgorithm.HS256, env.get("SECRET_KEY"))
                .compact();
    }

    // 토큰 해석
    public String resolveToken (HttpServletRequest request) {
        String bearerToken = request.getHeader(Constants.HEADER_ACCESSTOKEN_KEY);
        if(bearerToken == null || !bearerToken.startsWith(Constants.BEARER)) {
            return new AppException(ErrorCode.RESOLVE_TOKEN_PROBLEM.getMessage(), ErrorCode.RESOLVE_TOKEN_PROBLEM).toString();
        }
        return bearerToken.substring(7);
    }

    // 토큰이 환경변수의 SECRET_KEY가 맞는지 확인
    public boolean validation (String token) {
        return Jwts.parser()
                .setSigningKey(env.get("SECRET_KEY"))
                .parseClaimsJws(token) != null;
    }

    // 토큰 만료 확인
    public boolean isExpired(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(env.get("SECRET_KEY"))
                    .parseClaimsJws(token)
                    .getBody()
                    .getExpiration()
                    .before(now);
        } catch (Exception e) {
            return false;
        }

    }

    // 토큰에서 이메일 가져오기
    public String getUserEmail(String token){
        try {
            return Jwts.parser()
                    .setSigningKey(env.get("SECRET_KEY"))
                    .parseClaimsJws(token)
                    .getBody()
                    .get("email", String.class);
        } catch (AppException e){
            return ErrorCode.USER_EMAIL_NOT_FOUND.toString();
        }
    }

    // 이메일을 통해 인증권한 가져오기
    public Authentication getAuthentication (String token) {
        String email = getUserEmail(token);
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

}
