package com.bcsdlab.internal.global;

public abstract class BcsdException extends RuntimeException {

    protected BcsdException() {
    }

    public abstract BcsdExceptionType getExceptionType();
}
