package com.bcsdlab.internal.dues.exception;

import com.bcsdlab.internal.global.exception.BcsdException;
import com.bcsdlab.internal.global.exception.BcsdExceptionType;

public class DuesException extends BcsdException {

    private final transient BcsdExceptionType exceptionType;

    public DuesException(BcsdExceptionType exceptionType) {
        this.exceptionType = exceptionType;
    }

    public BcsdExceptionType getExceptionType() {
        return exceptionType;
    }
}
