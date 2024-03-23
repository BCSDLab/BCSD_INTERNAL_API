package com.bcsdlab.internal.team.controller.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record TeamRequest(
    @Schema(example = "false", description = "삭제 여부")
    @NotNull Boolean isDeleted
) {

}
