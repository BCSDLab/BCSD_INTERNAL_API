package com.bcsdlab.internal.admin.controller.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record AdminMemberActiveRequest(
    @Schema(example = "true", description = "활동 여부")
    @NotNull
    Boolean isActive
) {

}
