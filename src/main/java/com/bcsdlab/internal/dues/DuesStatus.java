package com.bcsdlab.internal.dues;

import java.util.Arrays;

import com.bcsdlab.internal.member.exception.MemberException;
import com.bcsdlab.internal.member.exception.MemberExceptionType;

public enum DuesStatus {
    NOT_PAID,
    PAID,
    SKIP,
    ;

    public static DuesStatus from(String status) {
        return Arrays.stream(values())
            .filter(type -> type.name().equals(status))
            .findAny()
            .orElseThrow(() -> new MemberException(MemberExceptionType.TRACK_NOT_FOUND));
    }
}
