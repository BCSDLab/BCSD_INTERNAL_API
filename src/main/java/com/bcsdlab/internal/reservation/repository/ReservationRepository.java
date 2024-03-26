package com.bcsdlab.internal.reservation.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bcsdlab.internal.reservation.exception.ReservationException;
import com.bcsdlab.internal.reservation.exception.ReservationExceptionType;
import com.bcsdlab.internal.reservation.model.Reservation;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    default Reservation getById(Long id) {
        return this.findById(id)
            .orElseThrow(() -> new ReservationException(ReservationExceptionType.RESERVATION_NOT_FOUND));
    }

    List<Reservation> findAllByMemberId(Long memberId);

    List<Reservation> findAllByOrderByStartDateTimeAsc();
}
