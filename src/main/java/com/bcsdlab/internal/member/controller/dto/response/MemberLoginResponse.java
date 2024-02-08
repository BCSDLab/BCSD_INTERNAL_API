package com.bcsdlab.internal.member.controller.dto.response;


import io.swagger.v3.oas.annotations.media.Schema;

public record MemberLoginResponse(
    @Schema(
        example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c",
        description = "accessToken"
    )
    String accessToken
) {

}
