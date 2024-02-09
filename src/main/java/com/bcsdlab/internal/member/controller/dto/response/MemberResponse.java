package com.bcsdlab.internal.member.controller.dto.response;


import java.time.LocalDate;
import java.time.LocalDateTime;

import com.bcsdlab.internal.member.Member;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;

public record MemberResponse(
    @Schema(example = "1", description = "사용자 고유 ID")
    Long id,

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Schema(example = "2023-01-01", description = "가입 일자")
    LocalDate joinDate,

    @Schema(example = "BACKEND", description = "트랙")
    String track,

    @Schema(example = "REGULAR", description = "회원 등급 (비기너, 레귤러, 멘토)")
    String memberType,

    @Schema(example = "군 휴학", description = "회원 상태")
    String status,

    @Schema(example = "최준호", description = "회원 이름")
    String name,

    @Schema(example = "한국기술교육대", description = "소속")
    String company,

    @Schema(example = "컴퓨터공학부", description = "학부")
    String department,

    @Schema(example = "2019136135", description = "학번")
    String studentNumber,

    @Schema(example = "010-1234-5678", description = "핸드폰 번호")
    String phoneNumber,

    @Schema(example = "junho5336@gmail.com", description = "이메일")
    String email,

    @Schema(example = "MANAGER", description = "회원 권한")
    String authority,

    @Schema(example = "CHOI-JJUNHO", description = "깃허브 이름")
    String githubName,

    @Schema(example = "2024-02-01 21:22:10", description = "생성 일자")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime createdAt,

    @Schema(example = "2024-02-01 21:22:10", description = "수정 일자")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime updatedAt,

    @Schema(example = "true", description = "승인 여부")
    boolean isAuthed,

    @Schema(example = "false", description = "삭제 여부")
    boolean isDeleted
) {

    public static MemberResponse from(Member member) {
        return new MemberResponse(
            member.getId(),
            member.getJoinDate(),
            member.getTrack().name(),
            member.getMemberType().name(),
            member.getStatus().getView(),
            member.getName(),
            member.getCompany(),
            member.getDepartment(),
            member.getStudentNumber(),
            member.getPhoneNumber(),
            member.getEmail(),
            member.getAuthority().name(),
            member.getGithubName(),
            member.getCreatedAt(),
            member.getUpdatedAt(),
            member.isAuthed(),
            member.isDeleted()
        );
    }
}
