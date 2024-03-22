package com.bcsdlab.internal.auth;

import java.util.Arrays;

import com.bcsdlab.internal.member.exception.MemberException;
import com.fasterxml.jackson.annotation.JsonCreator;

import static com.bcsdlab.internal.member.exception.MemberExceptionType.MEMBER_AUTHORITY_NOT_FOUND;
import lombok.Getter;

@Getter
public enum Authority {
    NORMAL,
    MANAGER,
    ADMIN,
    ;

    @JsonCreator
    public Authority from(String authority) {
        return Arrays.stream(values())
            .filter(type -> type.name().equals(authority.toUpperCase()))
            .findAny()
            .orElseThrow(() -> new MemberException(MEMBER_AUTHORITY_NOT_FOUND.withDetail(authority)));
    }
}
