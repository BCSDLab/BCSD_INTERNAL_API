package com.bcsdlab.internal.member.controller.dto.response;

import java.time.LocalDateTime;

import com.bcsdlab.internal.member.model.Member;
import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import io.swagger.v3.oas.annotations.media.Schema;

@JsonNaming(value = SnakeCaseStrategy.class)
public record HomepageMemberResponse(

    @Schema(example = "1", description = "사용자 고유 ID")
    Long id,

    @Schema(example = "이현수", description = "이름")
    String name,

    @Schema(example = "2020000000", description = "학번")
    String studentNumber,

    @Schema(example = "1", description = "사용자 고유 ID")
    String track,

    @Schema(example = "1", description = "사용자 고유 ID")
    String position,

    @Schema(example = "1", description = "사용자 고유 ID")
    String email,

    @Schema(example = "1", description = "사용자 고유 ID")
    String imageUrl,

    @Schema(example = "1", description = "사용자 고유 ID")
    boolean idDeleted,

    @Schema(example = "1", description = "사용자 고유 ID")
    LocalDateTime createdAt,

    @Schema(example = "1", description = "사용자 고유 ID")
    LocalDateTime updatedAt

) {

    public static HomepageMemberResponse from(Member member) {
        return new HomepageMemberResponse(
            member.getId(),
            member.getName(),
            member.getStudentNumber(),
            member.getTrack().getName(),
            member.getMemberType().toString(),
            member.getEmail(),
            member.getProfileImageUrl(),
            member.isDeleted(),
            member.getCreatedAt(),
            member.getUpdatedAt()
        );
    }
}
