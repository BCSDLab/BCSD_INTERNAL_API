package com.bcsdlab.internal.member.controller.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record MemberEmailRequest(
    @Schema(example = "example@koreatech.ac.kr", description = "이메일")
    @NotBlank String email
) {

}
