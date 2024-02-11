package com.bcsdlab.internal.dues.controller.dto.request;

import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import com.bcsdlab.internal.dues.Dues;
import com.bcsdlab.internal.dues.DuesStatus;
import com.bcsdlab.internal.member.Member;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record DuesCreateRequest(
    @Schema(example = "1", description = "회원 ID")
    Long memberId,

    @DateTimeFormat(pattern = "yyyy-MM")
    LocalDateTime date,

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
            date,
            status,
            false
        );
    }
}
