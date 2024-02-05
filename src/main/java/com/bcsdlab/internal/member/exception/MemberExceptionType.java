package com.bcsdlab.internal.member.exception;

import org.springframework.http.HttpStatus;

import com.bcsdlab.internal.global.BcsdExceptionType;

public enum MemberExceptionType implements BcsdExceptionType {
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "회원을 찾을 수 없습니다."),
    INVALID_LOGIN(HttpStatus.UNAUTHORIZED, "잘못된 아이디 혹은 비밀번호입니다."),
    ;

    private final HttpStatus status;
    private final String message;

    MemberExceptionType(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    @Override
    public HttpStatus getHttpStatus() {
        return this.status;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
