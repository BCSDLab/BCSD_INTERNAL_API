package com.bcsdlab.internal.team.controller;

import java.util.List;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.bcsdlab.internal.auth.Auth;
import com.bcsdlab.internal.team.controller.dto.Request.TeamCreateRequest;
import com.bcsdlab.internal.team.controller.dto.Request.TeamMapCreateRequest;
import com.bcsdlab.internal.team.controller.dto.Request.TeamModifyRequest;
import com.bcsdlab.internal.team.controller.dto.Request.TeamRequest;
import com.bcsdlab.internal.team.controller.dto.Response.TeamMemberResponse;
import com.bcsdlab.internal.team.controller.dto.Response.TeamResponse;

import static com.bcsdlab.internal.auth.Authority.ADMIN;
import static com.bcsdlab.internal.auth.Authority.MANAGER;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "팀 관리 API")
public interface TeamApi {

    @ApiResponses(
        value = {
            @ApiResponse(responseCode = "200"),
            @ApiResponse(
                responseCode = "404",
                content = @Content(schema = @Schema(hidden = true))
            ),
        }
    )
    @Operation(summary = "팀 조회")
    @GetMapping
    ResponseEntity<List<TeamResponse>> getTeam(
        @ParameterObject
        @ModelAttribute TeamRequest teamRequest
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
    @Operation(summary = "팀 수정")
    @PutMapping
    ResponseEntity<Void> modifyTeam(
        @Auth(permit = {MANAGER, ADMIN}) Long memberId,
        @RequestBody @Valid TeamModifyRequest teamModifyRequest
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
    @Operation(summary = "팀 삭제")
    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteTeam(
        @Auth(permit = {MANAGER, ADMIN}) Long memberId,
        @PathVariable Long id
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
    @Operation(summary = "팀 생성")
    @PostMapping
    ResponseEntity<Void> createTeam(
        @Auth(permit = {MANAGER, ADMIN}) Long memberId,
        @RequestBody TeamCreateRequest teamCreateRequest
    );

    @ApiResponses(
        value = {
            @ApiResponse(responseCode = "200"),
            @ApiResponse(
                responseCode = "404",
                content = @Content(schema = @Schema(hidden = true))
            ),
        }
    )
    @Operation(summary = "팀에 속한 멤버 전체 조회")
    @GetMapping("/members")
    ResponseEntity<List<TeamMemberResponse>> getTeamMember(
        @PageableDefault Pageable pageable
    );

    @ApiResponses(
        value = {
            @ApiResponse(responseCode = "200"),
            @ApiResponse(
                responseCode = "404",
                content = @Content(schema = @Schema(hidden = true))
            ),
        }
    )
    @Operation(summary = "특정 팀에 속한 멤버 전체 조회")
    @GetMapping("/members/{teamId}")
    ResponseEntity<List<TeamMemberResponse>> getTeamMemberByTeamId(
        @PathVariable Long teamId,
        @PageableDefault Pageable pageable
    );

    @ApiResponses(
        value = {
            @ApiResponse(responseCode = "200"),
            @ApiResponse(
                responseCode = "404",
                content = @Content(schema = @Schema(hidden = true))
            ),
        }
    )
    @Operation(summary = "특정 팀에 멤버 등록")
    @PostMapping("/members")
    ResponseEntity<Void> createTeamMember(
        @Auth(permit = {MANAGER, ADMIN}) Long memberId,
        @RequestBody TeamMapCreateRequest teamMapCreateRequest
    );

    @ApiResponses(
        value = {
            @ApiResponse(responseCode = "200"),
            @ApiResponse(
                responseCode = "404",
                content = @Content(schema = @Schema(hidden = true))
            ),
        }
    )
    @Operation(summary = "특정 팀에 멤버 삭제")
    @DeleteMapping("/members/{id}")
    ResponseEntity<Void> deleteTeamMember(
        @Auth(permit = {MANAGER, ADMIN}) Long memberId,
        @PathVariable Long id
    );
}
