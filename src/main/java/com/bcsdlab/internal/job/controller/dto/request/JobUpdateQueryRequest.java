package com.bcsdlab.internal.job.controller.dto.request;

import java.time.YearMonth;

import com.bcsdlab.internal.job.Job;
import com.bcsdlab.internal.member.model.Member;

import io.swagger.v3.oas.annotations.media.Schema;

public record JobUpdateQueryRequest(
    @Schema(description = "직무 ID", example = "1")
    Long id,
    @Schema(description = "회원 ID", example = "1")
    Long memberId,
    @Schema(description = "직무 유형", example = "회장")
    String type,
    @Schema(description = "시작 년도", example = "2021")
    Integer startYear,
    @Schema(description = "시작 월", example = "1")
    Integer startMonth,
    @Schema(description = "종료 년도", example = "2022")
    Integer endYear,
    @Schema(description = "종료 월", example = "1")
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
