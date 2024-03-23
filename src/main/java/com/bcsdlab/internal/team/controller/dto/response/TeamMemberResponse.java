package com.bcsdlab.internal.team.controller.dto.response;

import com.bcsdlab.internal.member.controller.dto.response.MemberResponse;
import com.bcsdlab.internal.member.model.Member;
import com.bcsdlab.internal.team.model.Team;
import com.bcsdlab.internal.team.model.TeamMap;

import io.swagger.v3.oas.annotations.media.Schema;

public record TeamMemberResponse(
    @Schema(example = "1", description = "팀멤버ID")
    Long id,

    @Schema(example = "1", description = "팀ID")
    Long teamId,

    @Schema(example = "비지니스", description = "팀이름")
    String teamName,

    @Schema(example = "false", description = "리더 여부")
    boolean isLeader,

    @Schema(description = "팀원정보")
    MemberResponse memberResponse
) {
    public static TeamMemberResponse of(TeamMap teamMap) {
        Member member = teamMap.getMember();
        return new TeamMemberResponse(
            teamMap.getId(),
            teamMap.getTeam().getId(),
            teamMap.getTeam().getName(),
            teamMap.isLeader(),
            MemberResponse.from(member)
        );
    }
}
