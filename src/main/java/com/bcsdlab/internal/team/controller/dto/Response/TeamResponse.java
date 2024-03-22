package com.bcsdlab.internal.team.controller.dto.Response;

import com.bcsdlab.internal.member.controller.dto.response.MemberResponse;
import com.bcsdlab.internal.team.model.Team;

import io.swagger.v3.oas.annotations.media.Schema;

public record TeamResponse(
    @Schema(example = "1", description = "팀id")
    Long id,

    @Schema(example = "Business", description = "팀 이름")
    String name,

    @Schema(description = "팀 리더")
    MemberResponse memberResponse
) {

    public static TeamResponse of(Team team) {
        return new TeamResponse(
            team.getId(),
            team.getName(),
            MemberResponse.from(team.getMember())
        );
    }
}
