package com.bcsdlab.internal.track.controller.dto.response;

import com.bcsdlab.internal.member.Member;
import com.bcsdlab.internal.member.controller.dto.response.MemberResponse;
import com.bcsdlab.internal.track.Track;

import io.swagger.v3.oas.annotations.media.Schema;

public record TrackWithLeaderResponse(
    @Schema(example = "1", description = "트랙 ID")
    Long id,

    @Schema(example = "BACKEND", description = "트랙 이름")
    String name,

    @Schema(description = "트랙장")
    MemberResponse leader
) {

    public static TrackWithLeaderResponse of(Track track, Member member) {
        return new TrackWithLeaderResponse(
            track.getId(),
            track.getName(),
            member == null ? null : MemberResponse.from(member)
        );
    }
}
