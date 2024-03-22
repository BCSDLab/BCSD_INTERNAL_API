package com.bcsdlab.internal.member.controller;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.bcsdlab.internal.auth.Auth;
import com.bcsdlab.internal.global.controller.dto.PageResponse;
import com.bcsdlab.internal.member.controller.dto.request.MemberEmailRequest;
import com.bcsdlab.internal.member.controller.dto.request.MemberLoginRequest;
import com.bcsdlab.internal.member.controller.dto.request.MemberQueryRequest;
import com.bcsdlab.internal.member.controller.dto.request.MemberRegisterRequest;
import com.bcsdlab.internal.member.controller.dto.request.MemberResetPasswordRequest;
import com.bcsdlab.internal.member.controller.dto.request.MemberResetTokenRequest;
import com.bcsdlab.internal.member.controller.dto.request.MemberUpdateRequest;
import com.bcsdlab.internal.member.controller.dto.response.MemberLoginResponse;
import com.bcsdlab.internal.member.controller.dto.response.MemberResponse;

import static com.bcsdlab.internal.auth.Authority.ADMIN;
import static com.bcsdlab.internal.auth.Authority.MANAGER;
import static com.bcsdlab.internal.auth.Authority.NORMAL;
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

@Tag(name = "회원 관리 API")
public interface MemberApi {

    @ApiResponses(
        value = {
            @ApiResponse(responseCode = "200"),
            @ApiResponse(
                responseCode = "400",
                content = @Content(schema = @Schema(hidden = true))
            ),
        }
    )
    @Operation(summary = "로그인")
    @PostMapping("/login")
    ResponseEntity<MemberLoginResponse> login(
        @RequestBody @Valid MemberLoginRequest request
    );

    @ApiResponses(
        value = {
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "400"),
        }
    )
    @Operation(summary = "회원가입")
    @PostMapping("/register")
    ResponseEntity<Void> register(
        @RequestBody @Valid MemberRegisterRequest request
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
    @Operation(summary = "내 정보 확인")
    @SecurityRequirement(name = "JWT")
    @GetMapping("/me")
    ResponseEntity<MemberResponse> getMemberMe(
        @Auth(permit = {NORMAL, MANAGER, ADMIN}) Long memberId
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
    @Operation(summary = "회원정보 전체조회")
    @SecurityRequirement(name = "JWT")
    @GetMapping
    ResponseEntity<PageResponse<MemberResponse>> getMembers(
        @Auth(permit = {NORMAL, MANAGER, ADMIN}) Long memberId,

        @ParameterObject
        @PageableDefault Pageable pageable,

        @ParameterObject
        @ModelAttribute MemberQueryRequest request
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
    @Operation(summary = "회원 정보 확인")
    @SecurityRequirement(name = "JWT")
    @GetMapping("/{memberId}")
    ResponseEntity<MemberResponse> getMemberById(
        @Auth(permit = {NORMAL, MANAGER, ADMIN}) Long id,
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
    @Operation(summary = "본인 정보 수정")
    @SecurityRequirement(name = "JWT")
    @PutMapping
    ResponseEntity<MemberResponse> updateMemberMe(
        @Auth(permit = {NORMAL, MANAGER, ADMIN}) Long memberId,
        @RequestBody @Valid MemberUpdateRequest request
    );

    @ApiResponses(
        value = {
            @ApiResponse(responseCode = "200"),
            @ApiResponse(
                responseCode = "400",
                content = @Content(schema = @Schema(hidden = true))
            ),
        }
    )
    @Operation(summary = "비밀번호 변경 요청 (이메일 전송)")
    @PostMapping("/password/change")
    ResponseEntity<Void> requestResetPassword(
        @RequestBody @Valid MemberEmailRequest request
    );

    @ApiResponses(
        value = {
            @ApiResponse(responseCode = "200"),
            @ApiResponse(
                responseCode = "400",
                content = @Content(schema = @Schema(hidden = true))
            ),
        }
    )
    @Operation(summary = "비밀번호 변경 이메일 인증번호 확인")
    @PostMapping("/password/certification")
    ResponseEntity<Void> certificateResetToken(
        @RequestBody @Valid MemberResetTokenRequest request
    );

    @ApiResponses(
        value = {
            @ApiResponse(responseCode = "200"),
            @ApiResponse(
                responseCode = "400",
                content = @Content(schema = @Schema(hidden = true))
            ),
        }
    )
    @Operation(summary = "비밀번호 변경 (이메일 인증 후)")
    @PostMapping("/password")
    ResponseEntity<Void> resetPassword(
        @RequestBody @Valid MemberResetPasswordRequest request
    );
}
