package com.bcsdlab.internal.bbot.controller.dto;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;

public record ThreadCreateRequest(
    @Schema(example = "https://github.com/BCSDLab/BCSD_INTERNAL_API ", description = "pr링크")
    @Size(max = 255) String pullRequestLink,

    @Schema(description = "리뷰어")
    List<String> reviewers,

    @Schema(example = "이현수", description = "작성자")
    @Size(max = 255) String writer,

    @Schema(example = "2024-03-31 9:27", description = "생성 일시")
    @Size(max = 255) String ts
) {
}
