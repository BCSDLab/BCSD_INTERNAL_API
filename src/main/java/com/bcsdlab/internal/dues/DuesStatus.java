package com.bcsdlab.internal.dues;

import java.util.Arrays;

import com.bcsdlab.internal.dues.exception.DuesException;
import com.fasterxml.jackson.annotation.JsonCreator;

import static com.bcsdlab.internal.dues.exception.DuesExceptionType.DUES_STATUS_NOT_FOUND;

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
            .orElseThrow(() -> new DuesException(DUES_STATUS_NOT_FOUND.withDetail(status)));
    }
}
