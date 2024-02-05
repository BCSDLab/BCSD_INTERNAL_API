package com.bcsdlab.internal.member.exception;

import com.bcsdlab.internal.global.BcsdException;
import com.bcsdlab.internal.global.BcsdExceptionType;

public class MemberException extends BcsdException {

    private final transient BcsdExceptionType exceptionType;

    public MemberException(BcsdExceptionType exceptionType) {
        this.exceptionType = exceptionType;
    }

    public BcsdExceptionType getExceptionType() {
        return exceptionType;
    }
}
