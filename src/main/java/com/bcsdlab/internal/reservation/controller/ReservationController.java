package com.bcsdlab.internal.reservation.controller;

import static com.bcsdlab.internal.auth.Authority.*;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bcsdlab.internal.auth.Auth;
import com.bcsdlab.internal.reservation.controller.dto.request.ReservationCreateRequest;
import com.bcsdlab.internal.reservation.controller.dto.request.ReservationModifyRequest;
import com.bcsdlab.internal.reservation.controller.dto.response.ReservationResponse;
import com.bcsdlab.internal.reservation.service.ReservationService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reservations")
public class ReservationController implements ReservationApi {

    private  final ReservationService reservationService;

    @GetMapping
    public ResponseEntity<List<ReservationResponse>> getAllReservation(
        @Auth(permit = {NORMAL, MANAGER, ADMIN}) Long memberId
    ) {
        List<ReservationResponse> reservationResponses = reservationService.getAllReservation();
        return ResponseEntity.ok(reservationResponses);
    }

    @GetMapping("/member")
    public ResponseEntity<List<ReservationResponse>> getMemberReservation(
        @Auth(permit = {NORMAL, MANAGER, ADMIN}) Long memberId
    ) {
        List<ReservationResponse> reservationResponses = reservationService.getMemberReservation(memberId);
        return ResponseEntity.ok(reservationResponses);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> modifyReservation(
        @Auth(permit = {NORMAL, MANAGER, ADMIN}) Long memberId,
        @PathVariable Long id,
        @RequestBody ReservationModifyRequest reservationModifyRequest
    ) {
        System.out.println("fds");
        reservationService.modifyReservation(id, reservationModifyRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(
        @Auth(permit = {NORMAL, MANAGER, ADMIN}) Long memberId,
        @PathVariable Long id
    ) {
        reservationService.deleteReservation(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping
    public ResponseEntity<Void> createReservation(
        @Auth(permit = {NORMAL, MANAGER, ADMIN}) Long memberId,
        @RequestBody @Valid ReservationCreateRequest reservationCreateRequest
    ) {
        reservationService.createReservation(memberId, reservationCreateRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
