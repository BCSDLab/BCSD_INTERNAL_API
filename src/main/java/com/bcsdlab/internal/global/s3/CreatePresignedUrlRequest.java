package com.bcsdlab.internal.global.s3;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;

public record CreatePresignedUrlRequest(
    @Schema(description = "사용 가능한 확장자: jpg|gif|png|jpeg|webp", example = "image.png")
    @Pattern(regexp = "(\\S[\\s\\S]*\\S(?=\\.(jpg|gif|png|jpeg|webp))\\.\\2)")
    String fileName
) {

}
