package com.bcsdlab.internal.reservation.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bcsdlab.internal.member.MemberRepository;
import com.bcsdlab.internal.member.model.Member;
import com.bcsdlab.internal.reservation.controller.dto.request.ReservationCreateRequest;
import com.bcsdlab.internal.reservation.controller.dto.request.ReservationModifyRequest;
import com.bcsdlab.internal.reservation.controller.dto.response.ReservationResponse;
import com.bcsdlab.internal.reservation.exception.ReservationException;
import com.bcsdlab.internal.reservation.model.Reservation;
import com.bcsdlab.internal.reservation.repository.ReservationCustomRepository;
import com.bcsdlab.internal.reservation.repository.ReservationRepository;

import static com.bcsdlab.internal.reservation.exception.ReservationExceptionType.RESERVATION_EXIST;
import static com.bcsdlab.internal.reservation.exception.ReservationExceptionType.RESERVATION_TIME_BAD_REQUEST;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final MemberRepository memberRepository;
    private final ReservationCustomRepository reservationCustomRepository;

    public List<ReservationResponse> getAllReservation() {
        return reservationRepository.findAllByOrderByStartDateTimeAsc().stream().map(ReservationResponse::of).toList();
    }

    @Transactional
    public void deleteReservation(Long id) {
        reservationRepository.deleteById(id);
    }

    @Transactional
    public void createReservation(
        Long memberId,
        ReservationCreateRequest reservationCreateRequest
    ) {
        Member member = memberRepository.getById(memberId);
        boolean isExistReservation = reservationCustomRepository.isExistReservation(
            reservationCreateRequest.startDateTime(),
            reservationCreateRequest.endDateTime()
        );
        if (reservationCreateRequest.startDateTime().isAfter(reservationCreateRequest.endDateTime())) {
            throw new ReservationException(RESERVATION_TIME_BAD_REQUEST);
        }
        if (isExistReservation) {
            throw new ReservationException(RESERVATION_EXIST);
        }
        Reservation reservation = Reservation.builder()
            .reason(reservationCreateRequest.reason())
            .detailedReason(reservationCreateRequest.detailedReason())
            .member(member)
            .memberCount(reservationCreateRequest.memberCount())
            .startDateTime(reservationCreateRequest.startDateTime())
            .endDateTime(reservationCreateRequest.endDateTime())
            .build();
        reservationRepository.save(reservation);
    }

    @Transactional
    public void modifyReservation(Long id, ReservationModifyRequest reservationModifyRequest) {
        Reservation reservation = reservationRepository.getById(id);
        boolean isExistReservation = reservationCustomRepository.isExistReservationNotId(
            reservationModifyRequest.startDateTime(),
            reservationModifyRequest.endDateTime(),
            id
        );
        if (reservationModifyRequest.startDateTime().isAfter(reservationModifyRequest.endDateTime())) {
            throw new ReservationException(RESERVATION_TIME_BAD_REQUEST);
        }
        if (isExistReservation) {
            throw new ReservationException(RESERVATION_EXIST);
        }
        reservation.setDetailedReason(reservationModifyRequest.detailedReason());
        reservation.setReason(reservationModifyRequest.reason());
        reservation.setStartDateTime(reservationModifyRequest.startDateTime());
        reservation.setEndDateTime(reservationModifyRequest.endDateTime());
        reservation.setMemberCount(reservationModifyRequest.memberCount());
    }

    public List<ReservationResponse> getMemberReservation(Long memberId) {
        return reservationRepository.findAllByMemberId(memberId).stream().map(ReservationResponse::of).toList();
    }
}
