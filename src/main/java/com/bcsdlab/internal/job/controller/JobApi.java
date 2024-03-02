package com.bcsdlab.internal.job.controller;

import static com.bcsdlab.internal.auth.Authority.ADMIN;
import static com.bcsdlab.internal.auth.Authority.MANAGER;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import com.bcsdlab.internal.auth.Auth;
import com.bcsdlab.internal.job.controller.dto.request.JobCreateQueryRequest;
import com.bcsdlab.internal.job.controller.dto.request.JobDeleteQueryRequest;
import com.bcsdlab.internal.job.controller.dto.request.JobQueryRequest;
import com.bcsdlab.internal.job.controller.dto.request.JobUpdateQueryRequest;
import com.bcsdlab.internal.job.controller.dto.response.JobGroupResponse;
import com.bcsdlab.internal.job.controller.dto.response.JobResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "직책 관리 API")
@SecurityRequirement(name = "JWT")
public interface JobApi {

    @ApiResponses(
        value = {
            @ApiResponse(
                responseCode = "200"
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
    @Operation(summary = "직책 전체 조회")
    @GetMapping("/jobs")
    ResponseEntity<JobGroupResponse> getAll(
        @Auth(permit = {MANAGER, ADMIN}) Long memberId,

        @ParameterObject
        @ModelAttribute JobQueryRequest request
    );

    @ApiResponses(
        value = {
            @ApiResponse(
                responseCode = "200"
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
    @Operation(summary = "직책 생성")
    @PostMapping("/jobs")
    ResponseEntity<JobResponse> createJob(
        @Auth(permit = {MANAGER, ADMIN}) Long memberId,

        @ParameterObject
        @ModelAttribute JobCreateQueryRequest request
    );

    @ApiResponses(
        value = {
            @ApiResponse(
                responseCode = "200"
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
    @Operation(summary = "직책 수정 조회")
    @PutMapping("/jobs")
    ResponseEntity<JobResponse> updateJob(
        @Auth(permit = {MANAGER, ADMIN}) Long memberId,

        @ParameterObject
        @ModelAttribute JobUpdateQueryRequest request
    );

    @ApiResponses(
        value = {
            @ApiResponse(
                responseCode = "204"
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
    @Operation(summary = "직책 삭제")
    @DeleteMapping("/jobs")
    ResponseEntity<Void> deleteJob(
        @Auth(permit = {MANAGER, ADMIN}) Long memberId,

        @ParameterObject
        @ModelAttribute JobDeleteQueryRequest request
    );
}
