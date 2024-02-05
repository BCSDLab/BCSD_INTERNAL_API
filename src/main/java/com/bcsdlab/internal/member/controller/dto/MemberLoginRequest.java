package com.bcsdlab.internal.member.controller.dto;

import jakarta.validation.constraints.NotBlank;


public record MemberLoginRequest(
    @NotBlank String studentNumber,
    @NotBlank String password
) {

}
