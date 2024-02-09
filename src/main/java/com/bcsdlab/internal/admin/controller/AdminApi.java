package com.bcsdlab.internal.admin.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.bcsdlab.internal.admin.controller.dto.request.AdminMemberUpdateRequest;
import com.bcsdlab.internal.auth.Auth;
import com.bcsdlab.internal.member.controller.dto.response.MemberResponse;

import static com.bcsdlab.internal.auth.Authority.ADMIN;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import static io.swagger.v3.oas.annotations.enums.ParameterIn.PATH;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "어드민 API")
@SecurityRequirement(name = "JWT")
public interface AdminApi {

    @ApiResponses(
        value = {
            @ApiResponse(responseCode = "200"),
            @ApiResponse(
                responseCode = "401",
                content = @Content(schema = @Schema(hidden = true))
            ),
            @ApiResponse(
                responseCode = "403",
                content = @Content(schema = @Schema(hidden = true))
            ),
            @ApiResponse(
                responseCode = "404",
                content = @Content(schema = @Schema(hidden = true))
            ),
        }
    )
    @Operation(summary = "회원 수정")
    @PutMapping("/members/{memberId}")
    ResponseEntity<MemberResponse> changeMemberStatus(
        @Auth(permit = ADMIN) Long adminId,
        @PathVariable Long memberId,
        @RequestParam @Valid AdminMemberUpdateRequest request
    );

    @ApiResponses(
        value = {
            @ApiResponse(responseCode = "204"),
            @ApiResponse(
                responseCode = "401",
                content = @Content(schema = @Schema(hidden = true))
            ),
            @ApiResponse(
                responseCode = "403",
                content = @Content(schema = @Schema(hidden = true))
            ),
            @ApiResponse(
                responseCode = "404",
                content = @Content(schema = @Schema(hidden = true))
            ),
        }
    )
    @Operation(summary = "회원 삭제", description = "Soft Delete")
    @DeleteMapping("/members/{memberId}")
    ResponseEntity<Void> withdrawMember(
        @Auth(permit = ADMIN) Long adminId,
        @Parameter(in = PATH) @PathVariable Long memberId
    );

    @ApiResponses(
        value = {
            @ApiResponse(responseCode = "200"),
            @ApiResponse(
                responseCode = "401",
                content = @Content(schema = @Schema(hidden = true))
            ),
            @ApiResponse(
                responseCode = "403",
                content = @Content(schema = @Schema(hidden = true))
            ),
            @ApiResponse(
                responseCode = "404",
                content = @Content(schema = @Schema(hidden = true))
            ),
        }
    )
    @Operation(summary = "회원 가입 승인")
    @PatchMapping("/members/{memberId}/accept")
    ResponseEntity<Void> acceptMember(
        @Auth(permit = ADMIN) Long adminId,
        @PathVariable Long memberId
    );
}