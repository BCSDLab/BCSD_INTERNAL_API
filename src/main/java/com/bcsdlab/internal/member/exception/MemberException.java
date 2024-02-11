package com.bcsdlab.internal.member.exception;

import com.bcsdlab.internal.global.exception.BcsdException;

public class MemberException extends BcsdException {

    private final MemberExceptionType memberExceptionType;

    public MemberException(MemberExceptionType memberExceptionType) {
        this.memberExceptionType = memberExceptionType;
    }

    public MemberExceptionType getExceptionType() {
        return memberExceptionType;
    }
}
