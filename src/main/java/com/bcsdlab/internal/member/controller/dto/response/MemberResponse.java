package com.bcsdlab.internal.member.controller.dto.response;


import java.time.LocalDateTime;

import com.bcsdlab.internal.member.Member;
import com.fasterxml.jackson.annotation.JsonFormat;

public record MemberResponse(
    Long id,
    String year,
    String track,
    String memberType,
    String status,
    String name,
    String company,
    String department,
    String studentNumber,
    String phoneNumber,
    String email,
    String authority,
    String githubName,

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime createdAt,

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime updatedAt
) {

    public static MemberResponse from(Member member) {
        return new MemberResponse(
            member.getId(),
            member.getYear(),
            member.getTrack().name(),
            member.getMemberType().name(),
            member.getStatus(),
            member.getName(),
            member.getCompany(),
            member.getDepartment(),
            member.getStudentNumber(),
            member.getPhoneNumber(),
            member.getEmail(),
            member.getAuthority().name(),
            member.getGithubName(),
            member.getCreatedAt(),
            member.getUpdatedAt()
        );
    }
}
