package com.bcsdlab.internal.reservation.exception;

import org.springframework.http.HttpStatus;

import com.bcsdlab.internal.global.exception.BcsdExceptionType;

public enum ReservationExceptionType implements BcsdExceptionType {
    RESERVATION_NOT_FOUND(HttpStatus.NOT_FOUND, "예약을 찾을 수 없습니다."),
    RESERVATION_EXIST(HttpStatus.BAD_REQUEST, "이미 예약된 시간입니다."),
    RESERVATION_TIME_BAD_REQUEST(HttpStatus.BAD_REQUEST, "시작시간은 종료시간보다 이전이여야합니다."),
    RESERVATION_INVALID_AUTH(HttpStatus.BAD_REQUEST, "접근 권한이 없습니다.");


    private final HttpStatus status;
    private final String message;
    private String detailMessage;

    ReservationExceptionType(HttpStatus status, String message) {
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
    public ReservationExceptionType withDetail(String detailMessage) {
        this.detailMessage = detailMessage;
        return this;
    }
}
