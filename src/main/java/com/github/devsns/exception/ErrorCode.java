package com.github.devsns.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "일치하는 회원을 찾을 수 없습니다"),
    USER_ID_UNMATCHED(HttpStatus.CONFLICT, "유저 아이디가 일치하지 않습니다."),
    USER_EMAIL_NOT_FOUND(HttpStatus.NOT_FOUND, "이메일을 찾을 수 없습니다."),
    USER_EMAIL_DUPLICATED(HttpStatus.CONFLICT, "이미 가입한 이메일입니다."),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "유효하지 않은 비밀번호 입니다."),
    USERNAME_NOT_FOUND(HttpStatus.NOT_FOUND, "유저네임을 찾을 수 없습니다."),
    USERNAME_DUPLICATED(HttpStatus.CONFLICT, "이미 존재하는 닉네임입니다."),
    QUES_BOARD_NOT_FOUND(HttpStatus.NOT_FOUND, "질문 게시글을 찾을 수 없습니다."),
    FOLLOW_INVALID_REQUEST(HttpStatus.BAD_REQUEST, "자기 자신을 팔로우할 수 없습니다."),
    FOLLOW_DUPLICATED(HttpStatus.CONFLICT, "이미 팔로우한 유저입니다."),
    FOLLOW_NOT_FOUND(HttpStatus.NOT_FOUND, "팔로우 상태가 아닌 유저입니다."),
    BINDING_RESULT_ERROR(HttpStatus.BAD_REQUEST, "데이터 유효성에 문제가 있습니다.")
    ;

    private final HttpStatus httpStatus;
    private final String message;
}
