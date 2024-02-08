package com.bcsdlab.internal.member;

import java.util.Arrays;

import com.bcsdlab.internal.member.exception.MemberException;
import com.bcsdlab.internal.member.exception.MemberExceptionType;

public enum Track {
    UIUX,
    FRONTEND,
    BACKEND,
    GAME,
    ANDROID,
    IOS,
    ;

    public static Track from(String trackName) {
        return Arrays.stream(values())
            .filter(track -> track.name().equals(trackName))
            .findAny()
            .orElseThrow(() -> new MemberException(MemberExceptionType.TRACK_NOT_FOUND));
    }
}
