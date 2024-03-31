package com.bcsdlab.internal.bbot.exception;

import org.springframework.http.HttpStatus;

import com.bcsdlab.internal.global.exception.BcsdExceptionType;

public enum ThreadExceptionType implements BcsdExceptionType {
    THREAD_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 PR을 찾을 수 없습니다."),
    ;

    private final HttpStatus status;
    private final String message;
    private String detailMessage;

    ThreadExceptionType(HttpStatus status, String message) {
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
    public ThreadExceptionType withDetail(String detailMessage) {
        this.detailMessage = detailMessage;
        return this;
    }
}
