package com.bcsdlab.internal.member.controller.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;


public record MemberLoginRequest(
    @Schema(example = "2019136135", description = "학번은 아이디로 사용된다")
    @NotBlank String studentNumber,

    @Schema(example = "asdf1234!", description = "비밀번호 (SHA256으로 암호화)")
    @NotBlank String password
) {

}
