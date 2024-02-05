package com.bcsdlab.internal.auth.exception;

import org.springframework.http.HttpStatus;

import com.bcsdlab.internal.global.BcsdExceptionType;

public enum AuthExceptionType implements BcsdExceptionType {
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "토큰이 유효하지 않습니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "인증되지 않았습니다."),
    FORBIDDEN(HttpStatus.FORBIDDEN, "접근 권한이 없습니다."),
    ;

    private final HttpStatus status;
    private final String message;

    AuthExceptionType(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    @Override
    public HttpStatus getHttpStatus() {
        return status;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
