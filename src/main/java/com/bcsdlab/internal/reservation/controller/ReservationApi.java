package com.bcsdlab.internal.reservation.controller;

import static com.bcsdlab.internal.auth.Authority.*;
import static io.swagger.v3.oas.annotations.enums.ParameterIn.PATH;

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
import org.springframework.web.bind.annotation.RequestParam;

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
import com.bcsdlab.internal.reservation.controller.dto.request.ReservationCreateRequest;
import com.bcsdlab.internal.reservation.controller.dto.request.ReservationModifyRequest;
import com.bcsdlab.internal.reservation.controller.dto.response.ReservationResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "동아리방 예약 API")
public interface ReservationApi {

    @ApiResponses(
        value = {
            @ApiResponse(responseCode = "200"),
            @ApiResponse(
                responseCode = "400",
                content = @Content(schema = @Schema(hidden = true))
            ),
        }
    )
    @SecurityRequirement(name = "JWT")
    @GetMapping
    ResponseEntity<List<ReservationResponse>> getAllReservation(
        @Auth(permit = {NORMAL, MANAGER, ADMIN}) Long memberId
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
    @SecurityRequirement(name = "JWT")
    @PutMapping("/{id}")
    ResponseEntity<Void> modifyReservation(
        @Auth(permit = {NORMAL, MANAGER, ADMIN}) Long memberId,
        @PathVariable Long id,
        @RequestBody @Valid ReservationModifyRequest reservationModifyRequest
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
    @SecurityRequirement(name = "JWT")
    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteReservation(
        @Auth(permit = {NORMAL, MANAGER, ADMIN}) Long memberId,
        @PathVariable Long id
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
    @SecurityRequirement(name = "JWT")
    @PostMapping
    ResponseEntity<Void> createReservation(
        @Auth(permit = {NORMAL, MANAGER, ADMIN}) Long memberId,
        @RequestBody @Valid ReservationCreateRequest reservationCreateRequest
    );
}
