package com.bcsdlab.internal.admin.controller.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

public record AdminMemberDeleteRequest(
    @Schema(example = "너무 힘들어서 못하겠어요 ㅠ", description = "탈퇴 사유")
    String reason
) {

}
