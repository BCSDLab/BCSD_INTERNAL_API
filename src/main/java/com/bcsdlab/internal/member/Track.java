package com.bcsdlab.internal.member;

import java.util.Arrays;

import com.bcsdlab.internal.member.exception.MemberException;
import com.fasterxml.jackson.annotation.JsonCreator;

import static com.bcsdlab.internal.member.exception.MemberExceptionType.TRACK_NOT_FOUND;

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
            .orElseThrow(() -> new MemberException(TRACK_NOT_FOUND.withDetail(trackName)));
    }
}
