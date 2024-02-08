package com.bcsdlab.internal.dues;

import java.util.Arrays;

import com.bcsdlab.internal.dues.exception.DuesExceptionType;
import com.bcsdlab.internal.member.exception.MemberException;
import com.fasterxml.jackson.annotation.JsonCreator;

public enum DuesStatus {
    NOT_PAID,
    PAID,
    SKIP,
    ;

    @JsonCreator
    public static DuesStatus from(String status) {
        return Arrays.stream(values())
            .filter(type -> type.name().equals(status.toUpperCase()))
            .findAny()
            .orElseThrow(() -> new MemberException(DuesExceptionType.DUES_STATUS_NOT_FOUND));
    }
}
