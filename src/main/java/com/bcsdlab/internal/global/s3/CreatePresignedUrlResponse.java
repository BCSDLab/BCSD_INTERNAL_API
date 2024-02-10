package com.bcsdlab.internal.global.s3;

import io.swagger.v3.oas.annotations.media.Schema;

public record CreatePresignedUrlResponse(
    @Schema(example = "https://presined-url-path") String presignedUrl,
    @Schema(example = "example.png") String fileName
) {

}
