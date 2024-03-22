package com.bcsdlab.internal.job.controller.dto.response;

import java.util.List;

import com.bcsdlab.internal.job.Job;

import io.swagger.v3.oas.annotations.media.Schema;

public record JobGroupResponse(

    @Schema(example = "2021", description = "년도")
    Integer year,

    @Schema(description = "회비 납입여부 목록")
    List<InnerJobResponse> jobs
) {

    public static JobGroupResponse of(Integer year, List<Job> jobs) {
        return new JobGroupResponse(
            year,
            jobs.stream()
                .map(InnerJobResponse::from)
                .toList()
        );
    }

    private record InnerJobResponse(
        @Schema(description = "직책 ID")
        Long id,

        @Schema(description = "회원 ID")
        Long memberId,

        @Schema(description = "직책 이름")
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

        public static InnerJobResponse from(Job job) {
            return new InnerJobResponse(
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
}
