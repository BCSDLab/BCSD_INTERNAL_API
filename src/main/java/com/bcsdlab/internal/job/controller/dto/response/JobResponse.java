package com.bcsdlab.internal.job.controller.dto.response;

import com.bcsdlab.internal.job.Job;

import io.swagger.v3.oas.annotations.media.Schema;

public record JobResponse(
    @Schema(description = "직무 ID", example = "1")
    Long id,

    @Schema(description = "회원 ID", example = "1")
    Long memberId,

    @Schema(description = "직무 타입", example = "회장")
    String type,

    @Schema(description = "시작 년도", example = "2021")
    Integer startYear,

    @Schema(description = "시작 월", example = "1")
    Integer startMonth,

    @Schema(description = "종료 년도", example = "2021")
    Integer endYear,

    @Schema(description = "종료 월", example = "12")
    Integer endMonth
) {

    public static JobResponse from(Job job) {
        return new JobResponse(
            job.getId(),
            job.getMember().getId(),
            job.getType(),
            job.getStartDate().getYear(),
            job.getStartDate().getMonthValue(),
            job.getEndDate().getYear(),
            job.getEndDate().getMonthValue()
        );
    }
}
