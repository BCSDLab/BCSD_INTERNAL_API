package com.bcsdlab.internal.reservation.exception;

import com.bcsdlab.internal.global.exception.BcsdException;

public class ReservationException extends BcsdException {

    private final ReservationExceptionType memberExceptionType;

    public ReservationException(ReservationExceptionType memberExceptionType) {
        this.memberExceptionType = memberExceptionType;
    }

    public ReservationExceptionType getExceptionType() {
        return memberExceptionType;
    }
}
