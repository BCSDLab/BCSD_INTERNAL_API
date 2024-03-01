package com.bcsdlab.internal.dues.controller.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record DuesDeleteQueryRequest(
    @Schema(example = "2021", description = "년도")
    @NotNull Integer year,

    @Schema(example = "1", description = "월")
    @NotNull Integer month,

    @Schema(example = "1", description = "회원 고유 ID")
    @NotNull Long memberId
) {

}
