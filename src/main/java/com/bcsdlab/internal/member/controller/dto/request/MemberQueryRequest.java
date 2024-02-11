package com.bcsdlab.internal.member.controller.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

public record MemberQueryRequest(
    @Schema(example = "최준호", description = "이름")
    String name,

    @Schema(example = "1", description = "트랙 ID")
    Long trackId,

    @Schema(example = "true", description = "삭제 여부")
    Boolean deleted,

    @Schema(example = "true", description = "인증 여부")
    Boolean authed
) {

}
