package com.bcsdlab.internal.job.controller.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record JobQueryRequest(
    @Schema(example = "2021", description = "년도")
    @NotNull Integer year,

    @Schema(example = "1", description = "트랙 ID")
    Long trackId
) {

}
