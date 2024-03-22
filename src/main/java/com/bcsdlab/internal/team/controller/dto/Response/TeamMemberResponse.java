package com.bcsdlab.internal.team.controller.dto.Response;

import com.bcsdlab.internal.team.model.TeamMap;

import io.swagger.v3.oas.annotations.media.Schema;

public record TeamMemberResponse(
    @Schema(example = "1", description = "팀멤버ID")
    Long id,

    @Schema(description = "팀")
    TeamResponse teamResponse
) {

    public static TeamMemberResponse of(TeamMap teamMap) {
        return new TeamMemberResponse(
            teamMap.getId(),
            TeamResponse.of(teamMap.getTeam())
        );
    }
}
