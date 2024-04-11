package com.github.devsns.domain.oauth2;

import com.github.devsns.domain.oauth2.userInfo.GoogleOAuth2UserInfo;
import com.github.devsns.domain.oauth2.userInfo.KakaoOAuth2UserInfo;
import com.github.devsns.domain.oauth2.userInfo.OAuth2UserInfo;
import com.github.devsns.domain.user.entitiy.Role;
import com.github.devsns.domain.user.entitiy.SocialType;
import com.github.devsns.domain.user.entitiy.UserEntity;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;
import java.util.UUID;

@Getter
public class OAuthAttributes {

    private final String nameAttributeKey; // OAuth2 로그인 진행 시 키가 되는 필드 값, PK와 같은 의미
    private final OAuth2UserInfo oAuth2UserInfo; // 소셜 타입별 로그인 유저 정보(닉네임, 이메일, 프로필 사진 등등)

    @Builder
    private OAuthAttributes(String nameAttributeKey, OAuth2UserInfo oAuth2UserInfo) {
        this.nameAttributeKey = nameAttributeKey;
        this.oAuth2UserInfo = oAuth2UserInfo;
    }

    /*
    SocialType에 맞는 메소드 호출하여 OAuthAttributes 객체 반환
    파라미터 : userNameAttributeName -> OAuth2 로그인 시 키(PK)가 되는 값 / attributes : OAuth 서비스의 유저 정보들
    소셜별 of 메소드(ofGoogle, ofKaKao, ofNaver)들은 각각 소셜 로그인 API에서 제공하는
    회원의 식별값(id), attributes, nameAttributeKey를 저장 후 build
    */
    public static OAuthAttributes of(SocialType socialType,
                                     String userNameAttributeName, Map<String, Object> attributes) {
        if (socialType == SocialType.KAKAO) {
            return ofKakao(userNameAttributeName, attributes);
        }
        return ofGoogle(userNameAttributeName, attributes);
    }

    private static OAuthAttributes ofKakao(String userNameAttributeName, Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .nameAttributeKey(userNameAttributeName)
                .oAuth2UserInfo(new KakaoOAuth2UserInfo(attributes))
                .build();
    }

    public static OAuthAttributes ofGoogle(String userNameAttributeName, Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .nameAttributeKey(userNameAttributeName)
                .oAuth2UserInfo(new GoogleOAuth2UserInfo(attributes))
                .build();
    }

    /*
    of메소드로 OAuthAttributes 객체가 생성되어, 유저 정보들이 담긴 OAuth2UserInfo가 소셜 타입별로 주입된 상태
    OAuth2UserInfo에서 socialId(식별값), nickname, imageUrl을 가져와서 build
    email에는 UUID로 중복 없는 랜덤 값 생성
    */
    public UserEntity toEntity(SocialType socialType, OAuth2UserInfo oauth2UserInfo) {
        return UserEntity.builder()
                .socialType(socialType)
                .socialId(oauth2UserInfo.getId())
                .email(UUID.randomUUID() + "@socialUser.com")
                .password("oauthPassword!@")
                .username(oauth2UserInfo.getNickname())
                .imageUrl(oauth2UserInfo.getImageUrl())
                .role(Role.USER)
                .build();
    }

}
