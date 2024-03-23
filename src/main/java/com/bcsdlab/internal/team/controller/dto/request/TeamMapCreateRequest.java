package com.bcsdlab.internal.team.controller.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record TeamMapCreateRequest(
    @Schema(example = "1", description = "멤버ID")
    @NotNull Long memberId,

    @Schema(example = "false", description = "리더 여부")
    boolean isLeader,

    @Schema(example = "1", description = "팀ID")
    @NotNull Long teamId
) {

}
