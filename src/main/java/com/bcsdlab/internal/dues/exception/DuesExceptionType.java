package com.bcsdlab.internal.dues.exception;

import org.springframework.http.HttpStatus;

import com.bcsdlab.internal.global.exception.BcsdExceptionType;

public enum DuesExceptionType implements BcsdExceptionType {
    DUES_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 회비내역을 찾을 수 없습니다."),
    DUES_STATUS_NOT_FOUND(HttpStatus.BAD_REQUEST, "존재하지 않는 회비 납부 상태입니다."),
    DUES_ALREADY_EXIST(HttpStatus.CONFLICT, "이미 존재하는 회비 내역입니다."),
    ;

    private final HttpStatus status;
    private final String message;
    private String detailMessage;

    DuesExceptionType(HttpStatus status, String message) {
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
    public DuesExceptionType withDetail(String detailMessage) {
        this.detailMessage = detailMessage;
        return this;
    }
}
