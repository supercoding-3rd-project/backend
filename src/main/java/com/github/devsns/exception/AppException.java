package com.github.devsns.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AppException extends RuntimeException{
    private String message;
    private ErrorCode errorCode;
}
