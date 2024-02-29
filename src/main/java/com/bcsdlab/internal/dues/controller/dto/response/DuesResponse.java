package com.bcsdlab.internal.dues.controller.dto.response;

import com.bcsdlab.internal.dues.Dues;
import com.bcsdlab.internal.dues.DuesStatus;

public record DuesResponse(
    long id,
    DuesStatus status,
    String memo,
    Integer year,
    Integer month
) {

    public static DuesResponse from(Dues dues) {
        return new DuesResponse(
            dues.getId(),
            dues.getStatus(),
            dues.getMemo(),
            dues.getDate().getYear(),
            dues.getDate().getMonthValue()
        );
    }
}
