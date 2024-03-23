package com.bcsdlab.internal.team.controller.dto.response;

import java.util.List;

import com.bcsdlab.internal.member.controller.dto.response.MemberResponse;
import com.bcsdlab.internal.team.model.Team;
import com.bcsdlab.internal.team.model.TeamMap;

import io.swagger.v3.oas.annotations.media.Schema;

public record TeamResponse(
    @Schema(example = "1", description = "팀id")
    Long id,

    @Schema(example = "Business", description = "팀 이름")
    String name,

    @Schema(description = "팀장")
    List<MemberResponse> leaders

) {
    public static TeamResponse of(Team team, List<TeamMap> teamMaps) {
        return new TeamResponse(
            team.getId(),
            team.getName(),
            teamMaps.stream().map(MemberResponse::from).toList()
        );
    }
}
