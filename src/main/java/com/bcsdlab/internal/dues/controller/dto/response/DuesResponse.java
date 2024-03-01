package com.bcsdlab.internal.dues.controller.dto.response;

import com.bcsdlab.internal.dues.Dues;

public record DuesResponse(
    long id,
    String status,
    String memo,
    Integer year,
    Integer month
) {

    public static DuesResponse from(Dues dues) {
        return new DuesResponse(
            dues.getId(),
            dues.getStatus().name(),
            dues.getMemo(),
            dues.getDate().getYear(),
            dues.getDate().getMonthValue()
        );
    }
}
