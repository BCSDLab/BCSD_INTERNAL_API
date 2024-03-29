package com.bcsdlab.internal.dues.controller;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.bcsdlab.internal.auth.Auth;
import com.bcsdlab.internal.dues.controller.dto.request.DuesCreateRequest;
import com.bcsdlab.internal.dues.controller.dto.request.DuesDeleteQueryRequest;
import com.bcsdlab.internal.dues.controller.dto.request.DuesQueryRequest;
import com.bcsdlab.internal.dues.controller.dto.request.DuesUpdateQueryRequest;
import com.bcsdlab.internal.dues.controller.dto.request.DuesUpdateRequest;
import com.bcsdlab.internal.dues.controller.dto.response.DuesGroupResponse;
import com.bcsdlab.internal.dues.controller.dto.response.DuesResponse;

import static com.bcsdlab.internal.auth.Authority.ADMIN;
import static com.bcsdlab.internal.auth.Authority.MANAGER;
import static com.bcsdlab.internal.auth.Authority.NORMAL;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "회비 납부 관리 API")
@SecurityRequirement(name = "JWT")
public interface DuesApi {

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
    @Operation(summary = "회비 전체 조회")
    @GetMapping
    ResponseEntity<DuesGroupResponse> getAll(
        @Auth(permit = {NORMAL, MANAGER, ADMIN}) Long memberId,

        @ParameterObject
        @ModelAttribute DuesQueryRequest request
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
    @Operation(summary = "회비 수정 조회")
    @PutMapping
    ResponseEntity<DuesResponse> updateDues(
        @Auth(permit = {MANAGER, ADMIN}) Long memberId,

        @ParameterObject
        @ModelAttribute DuesUpdateQueryRequest queryRequest,

        @RequestBody @Valid DuesUpdateRequest updateRequest
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
    @Operation(summary = "회비 삭제")
    @DeleteMapping
    ResponseEntity<Void> deleteDues(
        @Auth(permit = {MANAGER, ADMIN}) Long memberId,

        @ParameterObject
        @ModelAttribute DuesDeleteQueryRequest queryRequest
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
    @Operation(summary = "회비 생성")
    @PostMapping
    ResponseEntity<DuesResponse> createDues(
        @Auth(permit = {MANAGER, ADMIN}) Long memberId,
        @RequestBody @Valid DuesCreateRequest request
    );
}
