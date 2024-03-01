package com.bcsdlab.internal.dues.controller.dto.request;

import java.time.YearMonth;

import com.bcsdlab.internal.dues.Dues;
import com.bcsdlab.internal.dues.DuesStatus;
import com.bcsdlab.internal.member.Member;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record DuesCreateRequest(
    @Schema(example = "1", description = "회원 ID")
    @NotNull Long memberId,

    @Schema(example = "2021", description = "년도")
    @NotNull Integer year,

    @Schema(example = "1", description = "월")
    @NotNull Integer month,

    @Schema(example = "SKIP", description = """
        NOT_PAID
        PAID
        SKIP
        """)
    @NotNull DuesStatus status,

    @Schema(example = "회장", description = "메모")
    String memo
) {

    public Dues toEntity(Member member) {
        return new Dues(
            memo,
            member,
            YearMonth.of(year, month),
            status,
            false
        );
    }
}
