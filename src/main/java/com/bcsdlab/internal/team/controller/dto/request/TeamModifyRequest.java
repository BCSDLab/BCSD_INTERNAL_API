package com.bcsdlab.internal.team.controller.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TeamModifyRequest(
    @Schema(example = "1", description = "팀id")
    @NotNull Long id,

    @Schema(example = "Business", description = "팀 이름")
    @NotBlank String name,

    @Schema(example = "false", description = "삭제 여부")
    @NotNull Boolean isDeleted
) {

}
