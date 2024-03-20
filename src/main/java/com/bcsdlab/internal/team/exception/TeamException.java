package com.bcsdlab.internal.team.exception;

import com.bcsdlab.internal.global.exception.BcsdException;

public class TeamException extends BcsdException {

    private final TeamExceptionType trackExceptionType;

    public TeamException(TeamExceptionType trackExceptionType) {
        this.trackExceptionType = trackExceptionType;
    }

    public TeamExceptionType getExceptionType() {
        return trackExceptionType;
    }
}
