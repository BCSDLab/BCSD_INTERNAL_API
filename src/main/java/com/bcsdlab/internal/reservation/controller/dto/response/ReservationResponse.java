package com.bcsdlab.internal.reservation.controller.dto.response;

import java.time.LocalDateTime;

import com.bcsdlab.internal.global.config.CustomLocalDateTimeSerializer;
import com.bcsdlab.internal.reservation.model.Reservation;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import io.swagger.v3.oas.annotations.media.Schema;

public record ReservationResponse(

    @Schema(example = "1", description = "예약 ID")
    Long id,

    @Schema(example = "1", description = "예약 ID")
    String memberName,

    @Schema(example = "10", description = "사용인원")
    Integer memberCount,

    @Schema(example = "회의", description = "사용 목적")
    String reason,

    @Schema(example = "인터널 프로젝트를 위한 주간회의", description = "사용 목적에 대한 상세설명")
    String detailedReason,

    @Schema(example = "2024-03-18 10:00", description = "예약 시작 시간")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    LocalDateTime startDateTime,

    @Schema(example = "2024-03-18 12:00", description = "예약 종료 시간")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    LocalDateTime endDateTime
) {

    public static ReservationResponse of(Reservation reservation) {
        return new ReservationResponse(
            reservation.getId(),
            reservation.getMember().getName(),
            reservation.getMemberCount(),
            reservation.getReason(),
            reservation.getDetailedReason(),
            reservation.getStartDateTime(),
            reservation.getEndDateTime()
        );
    }
}
