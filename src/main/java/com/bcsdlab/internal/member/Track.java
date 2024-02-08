package com.bcsdlab.internal.member;

import java.util.Arrays;

import com.bcsdlab.internal.member.exception.MemberException;
import com.bcsdlab.internal.member.exception.MemberExceptionType;
import com.fasterxml.jackson.annotation.JsonCreator;

public enum Track {
    UIUX,
    FRONTEND,
    BACKEND,
    GAME,
    ANDROID,
    IOS,
    ;

    @JsonCreator
    public static Track from(String trackName) {
        return Arrays.stream(values())
            .filter(track -> track.name().equals(trackName.toUpperCase()))
            .findAny()
            .orElseThrow(() -> new MemberException(MemberExceptionType.TRACK_NOT_FOUND));
    }
}
