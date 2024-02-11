package com.bcsdlab.internal.dues.controller.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

public record DuesFindRequest(
    @Schema(example = "2021", description = "년도")
    String year,

    @Schema(example = "BACKEND", description = "트랙")
    String track
) {

}
