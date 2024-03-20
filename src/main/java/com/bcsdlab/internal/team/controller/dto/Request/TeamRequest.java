package com.bcsdlab.internal.team.controller.dto.Request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TeamRequest(
    @Schema(example = "false", description = "삭제 여부")
    @NotNull Boolean isDeleted
) {

}
