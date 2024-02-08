package com.bcsdlab.internal.member;

import java.util.Arrays;

import com.bcsdlab.internal.member.exception.MemberException;
import com.bcsdlab.internal.member.exception.MemberExceptionType;
import com.fasterxml.jackson.annotation.JsonCreator;

public enum MemberType {
    BEGINNER,
    REGULAR,
    MENTOR,
    ;

    @JsonCreator
    public static MemberType from(String memberType) {
        return Arrays.stream(values())
            .filter(type -> type.name().equals(memberType.toUpperCase()))
            .findAny()
            .orElseThrow(() -> new MemberException(MemberExceptionType.TRACK_NOT_FOUND));
    }
}
