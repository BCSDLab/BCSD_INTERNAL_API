package com.bcsdlab.internal.track.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.bcsdlab.internal.auth.Auth;
import com.bcsdlab.internal.track.controller.dto.request.TrackCreateRequest;
import com.bcsdlab.internal.track.controller.dto.request.TrackUpdateRequest;
import com.bcsdlab.internal.track.controller.dto.response.TrackResponse;

import static com.bcsdlab.internal.auth.Authority.ADMIN;
import static com.bcsdlab.internal.auth.Authority.MANAGER;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "트랙 관리 API")
public interface TrackApi {

    @ApiResponses(
        value = {
            @ApiResponse(responseCode = "200"),
            @ApiResponse(
                responseCode = "404",
                content = @Content(schema = @Schema(hidden = true))
            ),
        }
    )
    @Operation(summary = "트랙 전체 조회")
    @GetMapping
    ResponseEntity<List<TrackResponse>> getTrack();

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
    @Operation(summary = "트랙 생성")
    @SecurityRequirement(name = "JWT")
    @PostMapping
    ResponseEntity<Void> createTrack(
        @Auth(permit = {MANAGER,ADMIN}) Long memberId,
        @RequestBody TrackCreateRequest request
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
    @Operation(summary = "트랙 수정")
    @SecurityRequirement(name = "JWT")
    @PutMapping("/{id}")
    ResponseEntity<TrackResponse> updateTrack(
        @Auth(permit = {MANAGER,ADMIN}) Long memberId,
        @PathVariable Long id,
        @RequestBody TrackUpdateRequest request
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
    @Operation(summary = "트랙 삭제")
    @SecurityRequirement(name = "JWT")
    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteTrack(
        @Auth(permit = {MANAGER,ADMIN}) Long memberId,
        @PathVariable Long id
    );
}
