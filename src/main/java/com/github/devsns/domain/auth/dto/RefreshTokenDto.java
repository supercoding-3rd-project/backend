package com.github.devsns.domain.auth.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RefreshTokenDto {

    private String email;
    private String refreshToken;
}
