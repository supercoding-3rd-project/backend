package com.github.devsns.domain.notifications.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class NotificationRequest {
    private Long id;
    @JsonProperty("read")
    private Boolean isRead;
}