package com.bcsdlab.internal.bbot.controller;

import static com.bcsdlab.internal.auth.Authority.*;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.bcsdlab.internal.auth.Auth;
import com.bcsdlab.internal.bbot.controller.dto.ThreadCreateRequest;
import com.bcsdlab.internal.bbot.controller.dto.ThreadResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "BBot API")
@SecurityRequirement(name = "JWT")
public interface BBotApi {

    @ApiResponses(
        value = {
            @ApiResponse(responseCode = "201"),
            @ApiResponse(
                responseCode = "400",
                content = @Content(schema = @Schema(hidden = true))
            ),
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
    @Operation(summary = "PR 생성")
    @PostMapping("/b-bot/pull-request/thread")
    ResponseEntity<Void> createThread(
        @Auth(permit = {MANAGER, ADMIN, NORMAL}) Long adminId,
        @RequestBody @Valid ThreadCreateRequest threadCreateRequest
    );

    @ApiResponses(
        value = {
            @ApiResponse(responseCode = "200"),
            @ApiResponse(
                responseCode = "400",
                content = @Content(schema = @Schema(hidden = true))
            ),
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
    @Operation(summary = "PR 조회")
    @PostMapping("/b-bot/pull-request/thread")
    ResponseEntity<ThreadResponse> getThread(
        @Auth(permit = {MANAGER, ADMIN, NORMAL}) Long adminId,
        @RequestParam("pullRequestLink") String pullRequestLink
    );
}
