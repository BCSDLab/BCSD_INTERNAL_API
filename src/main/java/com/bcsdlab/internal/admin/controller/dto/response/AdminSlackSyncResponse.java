package com.bcsdlab.internal.admin.controller.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record AdminSlackSyncResponse(
    @Schema(description = "슬랙 아이디가 업데이트된 사용자수", example = "100")
    Integer slackSyncMemberCount,

    @Schema(description = "이미지가 업데이트된 사용자수", example = "100")
    Integer imageSyncMemberCount
) {

}
