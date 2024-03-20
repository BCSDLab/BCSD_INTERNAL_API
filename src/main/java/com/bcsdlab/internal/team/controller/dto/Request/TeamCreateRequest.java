package com.bcsdlab.internal.team.controller.dto.Request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TeamCreateRequest(
    @Schema(example = "1", description = "팀장ID")
    @NotNull Long leaderId,

    @Schema(example = "Business", description = "팀 이름")
    @NotBlank String name
) {

}
