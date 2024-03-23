package com.bcsdlab.internal.team.controller.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record TeamCreateRequest(
    @Schema(example = "Business", description = "팀 이름")
    @NotBlank String name
) {

}
