package com.bcsdlab.internal.track.controller.dto.response;

import com.bcsdlab.internal.track.Track;

import io.swagger.v3.oas.annotations.media.Schema;

public record TrackResponse(
    @Schema(example = "1", description = "트랙 ID")
    Long id,

    @Schema(example = "BACKEND", description = "트랙 이름")
    String name
) {

    public static TrackResponse from(Track track) {
        return new TrackResponse(track.getId(), track.getName());
    }
}
