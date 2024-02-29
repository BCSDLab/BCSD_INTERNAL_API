package com.bcsdlab.internal.dues.controller.dto.request;

import com.bcsdlab.internal.dues.DuesStatus;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record DuesUpdateRequest(
    @Schema(example = "SKIP", description = """
        NOT_PAID
        PAID
        SKIP
        NONE
        """)
    DuesStatus status,

    @Schema(example = "회장", description = "메모")
    String memo
) {

}
