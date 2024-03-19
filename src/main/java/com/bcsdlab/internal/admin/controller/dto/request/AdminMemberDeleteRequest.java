package com.bcsdlab.internal.admin.controller.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record AdminMemberDeleteRequest(
    @NotNull
    @Schema(example = "1", description = "회원ID")
    Long id,

    @Schema(example = "너무 힘들어서 못하겠어요 ㅠ", description = "탈퇴 사유")
    String reason
) {
}
