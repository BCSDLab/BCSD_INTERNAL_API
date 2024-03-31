package com.bcsdlab.internal.bbot.exception;

import com.bcsdlab.internal.global.exception.BcsdException;

public class ThreadException extends BcsdException {

    private final ThreadExceptionType threadExceptionType;

    public ThreadException(ThreadExceptionType threadExceptionType) {
        this.threadExceptionType = threadExceptionType;
    }

    public ThreadExceptionType getExceptionType() {
        return threadExceptionType;
    }
}
