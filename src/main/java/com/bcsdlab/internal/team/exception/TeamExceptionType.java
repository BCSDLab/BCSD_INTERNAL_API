package com.bcsdlab.internal.team.exception;

import org.springframework.http.HttpStatus;

import com.bcsdlab.internal.global.exception.BcsdExceptionType;

public enum TeamExceptionType implements BcsdExceptionType {
    TEAM_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 팀을 찾을 수 없습니다.");

    private final HttpStatus status;
    private final String message;
    private String detailMessage;

    TeamExceptionType(HttpStatus status, String message) {
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
    public TeamExceptionType withDetail(String detailMessage) {
        this.detailMessage = detailMessage;
        return this;
    }
}
