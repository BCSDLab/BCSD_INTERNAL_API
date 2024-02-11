package com.bcsdlab.internal.track.exception;

import org.springframework.http.HttpStatus;

import com.bcsdlab.internal.global.exception.BcsdExceptionType;

public enum TrackExceptionType implements BcsdExceptionType {
    TRACK_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 트랙을 찾을 수 없습니다."),
    ;

    private final HttpStatus status;
    private final String message;
    private String detailMessage;

    TrackExceptionType(HttpStatus status, String message) {
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
    public TrackExceptionType withDetail(String detailMessage) {
        this.detailMessage = detailMessage;
        return this;
    }
}
