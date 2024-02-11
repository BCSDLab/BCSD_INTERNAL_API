package com.bcsdlab.internal.global.exception;

public abstract class BcsdException extends RuntimeException {

    protected BcsdException() {
    }

    public abstract BcsdExceptionType getExceptionType();
}
