package com.github.devsns.domain.user.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateUser {
    private String imageUrl;
    private String password;
    private String username;
}
