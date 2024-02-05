package com.bcsdlab.internal.member.controller.dto;

import com.bcsdlab.internal.member.Member;

import static com.bcsdlab.internal.auth.Authority.NORMAL;
import jakarta.validation.constraints.NotBlank;

public record MemberRegisterRequest(
    @NotBlank String studentNumber,
    @NotBlank String password,
    @NotBlank String email
) {

    public Member toEntity() {
        return new Member(
            studentNumber,
            password,
            email,
            NORMAL
        );
    }
}
