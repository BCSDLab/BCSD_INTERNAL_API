package com.bcsdlab.internal.auth.exception;

import com.bcsdlab.internal.global.exception.BcsdException;
import com.bcsdlab.internal.global.exception.BcsdExceptionType;

public class AuthException extends BcsdException {

    private final AuthExceptionType authExceptionType;

    public AuthException(AuthExceptionType authExceptionType) {
        this.authExceptionType = authExceptionType;
    }

    public BcsdExceptionType getExceptionType() {
        return authExceptionType;
    }
}
