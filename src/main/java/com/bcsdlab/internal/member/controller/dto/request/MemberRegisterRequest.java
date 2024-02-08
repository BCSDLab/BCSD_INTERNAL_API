package com.bcsdlab.internal.member.controller.dto.request;

import com.bcsdlab.internal.member.Member;
import com.bcsdlab.internal.member.MemberType;
import com.bcsdlab.internal.member.Track;

import jakarta.validation.constraints.NotBlank;

public record MemberRegisterRequest(
    @NotBlank String year,
    @NotBlank String track,
    @NotBlank String memberType,
    @NotBlank String status,
    @NotBlank String name,
    @NotBlank String company,
    @NotBlank String department,
    @NotBlank String studentNumber,
    @NotBlank String phoneNumber,
    @NotBlank String email,
    @NotBlank String password,
    @NotBlank String githubName
) {

    public Member toEntity() {
        return new Member(
            year,
            Track.from(track),
            MemberType.from(memberType),
            status,
            name,
            company,
            department,
            studentNumber,
            phoneNumber,
            email,
            password,
            githubName
        );
    }
}
