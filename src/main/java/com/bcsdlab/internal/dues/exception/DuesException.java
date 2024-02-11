package com.bcsdlab.internal.dues.exception;

import com.bcsdlab.internal.global.exception.BcsdException;

public class DuesException extends BcsdException {

    private final DuesExceptionType duesExceptionType;

    public DuesException(DuesExceptionType duesExceptionType) {
        this.duesExceptionType = duesExceptionType;
    }

    public DuesExceptionType getExceptionType() {
        return duesExceptionType;
    }
}
