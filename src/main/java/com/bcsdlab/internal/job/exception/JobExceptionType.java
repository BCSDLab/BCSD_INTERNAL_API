package com.bcsdlab.internal.job.exception;

import org.springframework.http.HttpStatus;

import com.bcsdlab.internal.global.exception.BcsdExceptionType;

public enum JobExceptionType implements BcsdExceptionType {
    JOB_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 직무입니다."),
    ;

    private final HttpStatus status;
    private final String message;
    private String detailMessage;

    JobExceptionType(HttpStatus status, String message) {
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
    public JobExceptionType withDetail(String detailMessage) {
        this.detailMessage = detailMessage;
        return this;
    }
}
