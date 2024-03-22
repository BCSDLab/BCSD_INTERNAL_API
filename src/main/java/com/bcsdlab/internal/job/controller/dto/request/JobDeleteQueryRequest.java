package com.bcsdlab.internal.job.controller.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

public record JobDeleteQueryRequest(
    @Schema(description = "직무 ID", example = "1")
    Long id
) {

}
