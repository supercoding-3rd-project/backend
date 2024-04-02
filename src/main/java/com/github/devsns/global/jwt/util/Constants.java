package com.github.devsns.global.jwt.util;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class Constants {

    public static final String HEADER_ACCESSTOKEN_KEY = "Authorization";
    public static final String HEADER_REFRESHTOKEN_KEY = "refresh_token";
    public static final String BEARER = "Bearer ";
    public static final Long ACCESSTOKENVAILIDMILLISECOND = 1000L * 60 * 60 * 24 * 7; // 7일
    public static final Long REFRESHTOKENVAILIDMILLISECOND = 1000L * 60 * 60 * 24 * 15; // 15일
}
