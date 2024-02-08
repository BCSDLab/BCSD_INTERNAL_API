package com.bcsdlab.internal.member;

import java.util.Arrays;

import com.bcsdlab.internal.member.exception.MemberException;
import com.bcsdlab.internal.member.exception.MemberExceptionType;

public enum MemberType {
    BEGINNER,
    REGULAR,
    MENTOR,
    ;

    public static MemberType from(String memberType) {
        return Arrays.stream(values())
            .filter(type -> type.name().equals(memberType))
            .findAny()
            .orElseThrow(() -> new MemberException(MemberExceptionType.TRACK_NOT_FOUND));
    }
}
