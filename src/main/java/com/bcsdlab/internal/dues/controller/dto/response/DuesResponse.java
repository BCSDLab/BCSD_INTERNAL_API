package com.bcsdlab.internal.dues.controller.dto.response;

import java.time.YearMonth;

import com.bcsdlab.internal.dues.Dues;
import com.fasterxml.jackson.annotation.JsonFormat;

public record DuesResponse(
    long id,
    String status,
    String memo,
    @JsonFormat(pattern = "yyyy-MM")
    YearMonth month
) {

    public static DuesResponse from(Dues dues) {
        return new DuesResponse(
            dues.getId(),
            dues.getStatus().name(),
            dues.getMemo(),
            dues.getDate()
        );
    }
}
