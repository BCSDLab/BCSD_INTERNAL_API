package com.bcsdlab.internal.reservation.controller.dto.request;

import java.time.LocalDateTime;

import com.bcsdlab.internal.global.config.CustomLocalDateTimeDeserializer;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@JsonNaming(value = SnakeCaseStrategy.class)
public record ReservationModifyRequest(

    @NotNull
    @Schema(example = "10", description = "사용인원")
    Integer memberCount,

    @NotBlank
    @Schema(example = "회의", description = "사용 목적")
    String reason,

    @NotBlank
    @Schema(example = "인터널 프로젝트를 위한 주간회의", description = "사용 목적에 대한 상세설명")
    String detailedReason,

    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    @NotNull
    @Schema(example = "2024-03-18 10:00", description = "예약 시작 시간")
    LocalDateTime startDateTime,

    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    @NotNull
    @Schema(example = "2024-03-18 12:00", description = "예약 종료 시간")
    LocalDateTime endDateTime
) {

}
