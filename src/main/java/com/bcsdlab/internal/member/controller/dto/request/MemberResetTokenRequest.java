package com.bcsdlab.internal.member.controller.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

public record MemberResetTokenRequest(
    @Schema(example = "example@koreatech.ac.kr", description = "이메일")
    String email,

    @Schema(example = "123456", description = "인증번호")
    String token
) {
}
