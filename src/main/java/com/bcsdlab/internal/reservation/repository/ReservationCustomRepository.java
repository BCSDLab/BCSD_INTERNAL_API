package com.bcsdlab.internal.reservation.repository;

import java.time.LocalDateTime;
import java.util.List;

import com.bcsdlab.internal.job.Job;
import com.bcsdlab.internal.reservation.controller.dto.request.ReservationCreateRequest;
import com.bcsdlab.internal.reservation.model.Reservation;

public interface ReservationCustomRepository {

    boolean isExistReservation(LocalDateTime startDateTime, LocalDateTime endDateTime);

    boolean isExistReservationNotId(LocalDateTime startDateTime, LocalDateTime endDateTime, Long id);

}
