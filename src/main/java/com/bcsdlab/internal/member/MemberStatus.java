package com.bcsdlab.internal.member;

import java.util.Arrays;

import com.bcsdlab.internal.member.exception.MemberException;
import com.fasterxml.jackson.annotation.JsonCreator;

import static com.bcsdlab.internal.member.exception.MemberExceptionType.MEMBER_STATUS_NOT_FOUND;
import lombok.Getter;

@Getter
public enum MemberStatus {
    ATTEND("재학"),
    OFF("휴학"),
    IPP("현장실습"),
    ARMY("군 휴학"),
    COMPLETION("수료"),
    GRADUATE("졸업"),
    ;

    private final String view;

    MemberStatus(String view) {
        this.view = view;
    }

    @JsonCreator
    public MemberStatus from(String status) {
        return Arrays.stream(values())
            .filter(type -> type.name().equals(status.toUpperCase()))
            .findAny()
            .orElseThrow(() -> new MemberException(MEMBER_STATUS_NOT_FOUND.withDetail(status)));
    }
}
