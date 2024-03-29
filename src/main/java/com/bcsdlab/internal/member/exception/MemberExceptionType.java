package com.bcsdlab.internal.member.exception;

import org.springframework.http.HttpStatus;

import com.bcsdlab.internal.global.exception.BcsdExceptionType;

public enum MemberExceptionType implements BcsdExceptionType {
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "회원을 찾을 수 없습니다."),
    MEMBER_NOT_AUTHORIZED(HttpStatus.UNAUTHORIZED, "인증되지 않은 회원입니다."),
    INVALID_LOGIN(HttpStatus.UNAUTHORIZED, "잘못된 아이디 혹은 비밀번호입니다."),
    TRACK_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 트랙입니다."),
    MEMBER_STATUS_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 상태입니다."),
    MEMBER_AUTHORITY_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 권한입니다."),
    MEMBER_TYPE_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 사용자 타입입니다."),
    MEMBER_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 존재하는 회원입니다."),
    MEMBER_ALREADY_EXISTS_STUDENT_NUMBER(HttpStatus.CONFLICT, "이미 존재하는 학번입니다."),
    EMAIL_NOT_FOUND(HttpStatus.NOT_FOUND, "이메일을 찾을 수 없습니다."),
    MEMBER_ALREADY_EXISTS_EMAIL(HttpStatus.CONFLICT, "이미 존재하는 이메일입니다."),
    CERTIFICATION_CODE_NOT_MATCH(HttpStatus.BAD_REQUEST, "인증 번호가 일치하지 않습니다."),
    PASSWORD_RESET_TOKEN_NOT_FOUND(HttpStatus.NOT_FOUND, "비밀번호 변경 요청을 찾을 수 없습니다."),
    PASSWORD_SAME_AS_BEFORE(HttpStatus.BAD_REQUEST, "기존 비밀번호와 동일합니다."),
    PASSWORD_EMPTY(HttpStatus.BAD_REQUEST, "비밀번호가 비어있습니다."),
    ;

    private final HttpStatus status;
    private final String message;
    private String detailMessage;

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
        if (detailMessage == null) {
            return message;
        }
        return MESSAGE_FORMAT.formatted(message, detailMessage).strip();
    }

    @Override
    public MemberExceptionType withDetail(String detailMessage) {
        this.detailMessage = detailMessage;
        return this;
    }
}
