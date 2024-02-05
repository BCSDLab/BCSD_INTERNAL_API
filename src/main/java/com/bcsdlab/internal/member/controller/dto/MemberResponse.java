package com.bcsdlab.internal.member.controller.dto;


import com.bcsdlab.internal.member.Member;

public record MemberResponse(
    Long memberId,
    String studentNumber,
    String email
) {

    public static MemberResponse from(Member member) {
        return new MemberResponse(member.getId(), member.getStudentNumber(), member.getEmail());
    }
}
