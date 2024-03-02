package com.bcsdlab.internal.job.controller.dto.request;

import java.time.YearMonth;

import com.bcsdlab.internal.job.Job;
import com.bcsdlab.internal.member.Member;

import io.swagger.v3.oas.annotations.media.Schema;

public record JobCreateQueryRequest(
    @Schema(description = "회원 ID", example = "1")
    Long memberId,

    @Schema(description = "직무 이름", example = "회장")
    String type,

    @Schema(description = "직무 시작 년도", example = "2022")
    Integer startYear,

    @Schema(description = "직무 시작 월", example = "10")
    Integer startMonth,

    @Schema(description = "직무 종료 년도", example = "2023")
    Integer endYear,

    @Schema(description = "직무 종료 월", example = "10")
    Integer endMonth
) {

    public Job toEntity(Member member) {
        return new Job(
            member,
            type,
            YearMonth.of(startYear, startMonth),
            YearMonth.of(endYear, endMonth)
        );
    }
}
