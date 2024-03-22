package com.bcsdlab.internal.reservation.repository;

import java.time.LocalDateTime;

public interface ReservationCustomRepository {

    boolean isExistReservation(LocalDateTime startDateTime, LocalDateTime endDateTime);

    boolean isExistReservationNotId(LocalDateTime startDateTime, LocalDateTime endDateTime, Long id);

}
