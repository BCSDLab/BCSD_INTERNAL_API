package com.bcsdlab.internal.track.exception;

import com.bcsdlab.internal.global.exception.BcsdException;

public class TrackException extends BcsdException {

    private final TrackExceptionType trackExceptionType;

    public TrackException(TrackExceptionType trackExceptionType) {
        this.trackExceptionType = trackExceptionType;
    }

    public TrackExceptionType getExceptionType() {
        return trackExceptionType;
    }
}
